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

import org.jboss.aerogear.android.Config;
import org.jboss.aerogear.android.security.CryptoConfiguration;
import org.jboss.aerogear.android.security.EncryptionService;
import org.jboss.aerogear.crypto.RandomUtils;

/**
 * Configures an instance of {@link PassphraseEncryptionServices}.
 */
public final class PassphraseCryptoConfiguration extends CryptoConfiguration<PassphraseCryptoConfiguration> implements Config<PassphraseCryptoConfiguration> {

    private byte[] salt = RandomUtils.randomBytes();
    private String passphrase;

    /**
     * A cryptographic salt to be used by the Encryption Service to generate a
     * key.
     * 
     * @return the current salt.
     */
    public byte[] getSalt() {
        return salt;
    }

    /**
     * A cryptographic salt to be used by the Encryption Service to generate a
     * key.
     * 
     * @param salt a new salt
     * @return the current configuration
     */
    public PassphraseCryptoConfiguration setSalt(byte[] salt) {
        this.salt = salt;
        return this;
    }

    /**
     * The passphrase is phrase which will be used to generate a key.
     * 
     * @return the current passphrase
     */
    public String getPassphrase() {
        return passphrase;
    }

    /**
     * The passphrase is phrase which will be used to generate a key.
     * 
     * @param passphrase a new passphrase
     * @return the current configuration
     */
    public PassphraseCryptoConfiguration setPassphrase(String passphrase) {
        this.passphrase = passphrase;
        return this;
    }

    @Override
    protected EncryptionService buildService() {
        return new PassphraseEncryptionServices(this);
    }

}
