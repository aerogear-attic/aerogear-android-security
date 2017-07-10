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
package org.jboss.aerogear.android.security.passphrase;

import android.util.Log;
import org.jboss.aerogear.AeroGearCrypto;
import org.jboss.aerogear.android.security.AbstractEncryptionService;
import org.jboss.aerogear.android.security.EncryptionService;
import org.jboss.aerogear.crypto.CryptoBox;
import org.jboss.aerogear.crypto.keys.PrivateKey;
import org.jboss.aerogear.crypto.password.Pbkdf2;

import java.security.spec.InvalidKeySpecException;

/**
 * This class generates a CryptoBox from a PassPhrase
 */
public class PassphraseGeneratedEncryptionServices extends AbstractEncryptionService implements EncryptionService {

    private static final String TAG = PassphraseGeneratedEncryptionServices.class.getSimpleName();

    private final CryptoBox crypto;

    public PassphraseGeneratedEncryptionServices(PassphraseGeneratedEncryptionConfiguration config) {
        super(config.getContext());
        this.crypto = getCrypto(config);
    }

    private CryptoBox getCrypto(PassphraseGeneratedEncryptionConfiguration config) {
        Pbkdf2 pbkdf2 = AeroGearCrypto.pbkdf2();
        byte[] rawPassword;

        validate(config);

        try {
            rawPassword = pbkdf2.encrypt(config.getPassphrase(), config.getSalt());
            return new CryptoBox(new PrivateKey(rawPassword));
        } catch (InvalidKeySpecException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

    }

    private void validate(PassphraseGeneratedEncryptionConfiguration config) {

        if (config.getSalt() == null) {
            throw new IllegalArgumentException("The salt must not be null");
        }

        if (config.getPassphrase() == null) {
            throw new IllegalArgumentException("The passphrase must not be null");
        }
    }

    @Override
    protected CryptoBox getCryptoInstance() {
        return crypto;
    }

}
