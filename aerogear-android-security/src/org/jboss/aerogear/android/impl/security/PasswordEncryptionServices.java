/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android.impl.security;

import android.content.Context;
import android.content.SharedPreferences;
import org.jboss.aerogear.AeroGearCrypto;
import org.jboss.aerogear.android.security.CryptoConfig;
import org.jboss.aerogear.android.security.EncryptionService;
import org.jboss.aerogear.android.security.EncryptionServiceType;
import org.jboss.aerogear.crypto.CryptoBox;
import org.jboss.aerogear.crypto.RandomUtils;
import org.jboss.aerogear.crypto.encoders.Encoder;
import org.jboss.aerogear.crypto.encoders.Hex;
import org.jboss.aerogear.crypto.keys.KeyPair;
import org.jboss.aerogear.crypto.password.Pbkdf2;

import java.security.spec.InvalidKeySpecException;

/**
 * This class will build a CryptoBox including keys from a keystore protected
 * by a password.
 * 
 * If a keystore does not exist, one will be created and saved on the device.
 */
public class PasswordEncryptionServices extends AbstractEncryptionService implements EncryptionService {

    private static final String TAG = PasswordEncryptionServices.class.getSimpleName();
    private static final String APPLICATION_SALT_KEY = "applicationSALT";

    private final Context appContext;
    private final CryptoBox crypto;

    public PasswordEncryptionServices(PasswordProtectedKeystoreCryptoConfig config, Context appContext) {
        super(appContext);
        this.appContext = appContext;
        this.crypto = getCrypto(appContext, config);
    }

    private CryptoBox getCrypto(Context appContext, PasswordProtectedKeystoreCryptoConfig config) {
        validate(config);

        String keyAlias = config.getAlias();
        if (keyAlias == null) {
            throw new IllegalArgumentException("Alias in CryptoConfig may not be null");
        }

        char[] password = derive(config.password).toCharArray();

        KeyStoreServices keyStoreServices = new KeyStoreServices(appContext, password);
        byte[] keyEntry = keyStoreServices.getEntry(keyAlias);
        if (keyEntry != null) {
            return new CryptoBox(keyEntry);
        } else {
            return new CryptoBox(createKey(keyStoreServices, keyAlias));
        }

    }

    private byte[] createKey(KeyStoreServices keyStoreServices, String keyAlias) {
        KeyPair pair = new KeyPair();

        CryptoBox cryptoBox = new CryptoBox();
        byte[] sharedSecret = cryptoBox.generateSecret(pair.getPrivateKey(), pair.getPublicKey());

        keyStoreServices.addEntry(keyAlias, sharedSecret);
        keyStoreServices.save();

        return sharedSecret;
    }

    private void validate(PasswordProtectedKeystoreCryptoConfig config) {

        if (config.alias == null) {
            throw new IllegalArgumentException("The alias must not be null");
        }

        if (config.password == null) {
            throw new IllegalArgumentException("The password must not be null");
        }

        if (config.keyStoreFile == null) {
            throw new IllegalArgumentException("The keystoreFile must not be null");
        }
    }

    private String derive(String password) {
        Pbkdf2 pbkdf2 = AeroGearCrypto.pbkdf2();
        byte[] rawPassword = null;
        try {
            byte[] salt = getSalt();
            rawPassword = pbkdf2.encrypt(password, salt);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return Encoder.HEX.encode(rawPassword);
    }

    private byte[] getSalt() {
        byte[] salt;
        SharedPreferences preferences = appContext.getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        if (preferences.contains(APPLICATION_SALT_KEY)) {
            salt = new Hex().decode(preferences.getString(APPLICATION_SALT_KEY, ""));
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            salt = RandomUtils.randomBytes();
            editor.putString(APPLICATION_SALT_KEY, new Hex().encode(salt));
            editor.commit();
        }
        return salt;
    }

    @Override
    protected CryptoBox getCryptoInstance() {
        return crypto;
    }

    public static class PasswordProtectedKeystoreCryptoConfig implements CryptoConfig {
        private String alias;
        private String password;
        private String keyStoreFile = "default.keystore";

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public EncryptionServiceType getType() {
            return EncryptionServiceTypes.PASSWORD_KEYSTORE;
        }

    }

}
