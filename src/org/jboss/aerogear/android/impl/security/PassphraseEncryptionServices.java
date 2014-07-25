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
import android.util.Log;
import org.jboss.aerogear.AeroGearCrypto;
import org.jboss.aerogear.android.security.CryptoConfig;
import org.jboss.aerogear.android.security.EncryptionService;
import org.jboss.aerogear.android.security.EncryptionServiceType;
import org.jboss.aerogear.crypto.CryptoBox;
import org.jboss.aerogear.crypto.Random;
import org.jboss.aerogear.crypto.keys.PrivateKey;
import org.jboss.aerogear.crypto.password.Pbkdf2;

import java.security.spec.InvalidKeySpecException;

/**
 * This class generates a CryptoBox from a PassPhrase
 */
public class PassphraseEncryptionServices extends AbstractEncryptionService implements EncryptionService {

    private static final String TAG = PassphraseEncryptionServices.class.getSimpleName();

    private final CryptoBox crypto;

    public PassphraseEncryptionServices(Context appContext, PassPhraseCryptoConfig config) {
        super(appContext);
        this.crypto = getCrypto(config);
    }

    private CryptoBox getCrypto(PassPhraseCryptoConfig config) {
        Pbkdf2 pbkdf2 = AeroGearCrypto.pbkdf2();
        byte[] rawPassword;

        validate(config);

        try {
            rawPassword = pbkdf2.encrypt(config.passphrase, config.salt);
            return new CryptoBox(new PrivateKey(rawPassword));
        } catch (InvalidKeySpecException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

    }

    private void validate(PassPhraseCryptoConfig config) {

        if (config.salt == null) {
            throw new IllegalArgumentException("The salt must not be null");
        }

        if (config.passphrase == null) {
            throw new IllegalArgumentException("The passphrase must not be null");
        }
    }

    @Override
    protected CryptoBox getCryptoInstance() {
        return crypto;
    }

    public static class PassPhraseCryptoConfig implements CryptoConfig {
        private byte[] salt = new Random().randomBytes();
        private String passphrase;

        public byte[] getSalt() {
            return salt;
        }

        public void setSalt(byte[] salt) {
            this.salt = salt;
        }

        public String getPassphrase() {
            return passphrase;
        }

        public void setPassphrase(String passphrase) {
            this.passphrase = passphrase;
        }

        @Override
        public EncryptionServiceType getType() {
            return EncryptionServiceTypes.PASSPHRASE;
        }

    }

}
