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
package org.jboss.aerogear.android.security.keystore;

import org.jboss.aerogear.android.core.Config;
import org.jboss.aerogear.android.security.AbstractEncryptionConfiguration;
import org.jboss.aerogear.android.security.EncryptionService;

/**
 * Configures an instance of {@link KeyStoreBasedEncryptionEncryptionServices}.
 */
public final class KeyStoreBasedEncryptionConfiguration extends AbstractEncryptionConfiguration<KeyStoreBasedEncryptionConfiguration> implements
        Config<KeyStoreBasedEncryptionConfiguration> {

    private String alias;
    private String password;
    private String keyStoreFile = "default.keystore";

    /**
     * The alias of the key in the keystore.
     * 
     * @return the current alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * The alias of the key in the keystore.
     * 
     * @param alias a new alias
     * @return the current configuration
     */
    public KeyStoreBasedEncryptionConfiguration setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * The password is a String value protecting the keystore.
     * 
     * @return the current password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * The password is a String value protecting the keystore.
     * 
     * @param password a new password
     * @return the current configuration.
     */
    public KeyStoreBasedEncryptionConfiguration setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * The keystore file is the filename of the keystore. Defaults to `default.kestore`.
     * 
     * @return the current value
     */
    public String getKeyStoreFile() {
        return keyStoreFile;
    }

    /**
     * The keystore file is the filename of the keystore. Defaults to `default.kestore`.
     * 
     * @param keyStoreFile a new keystoreFile
     * @return the current configuration
     */
    public KeyStoreBasedEncryptionConfiguration setKeyStoreFile(String keyStoreFile) {
        this.keyStoreFile = keyStoreFile;
        return this;
    }

    @Override
    protected EncryptionService buildService() {
        return new KeyStoreBasedEncryptionEncryptionServices(this);
    }

}
