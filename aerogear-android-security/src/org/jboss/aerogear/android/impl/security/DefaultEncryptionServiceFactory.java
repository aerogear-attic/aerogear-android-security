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
import org.jboss.aerogear.android.security.CryptoConfig;
import org.jboss.aerogear.android.security.EncryptionService;
import org.jboss.aerogear.android.security.EncryptionServiceFactory;

public class DefaultEncryptionServiceFactory implements EncryptionServiceFactory {

    @Override
    public EncryptionService getService(CryptoConfig config, Context context) {

        if (EncryptionServiceTypes.PASSPHRASE.equals(config.getType())) {
            return makePassPhraseService(config, context);
        } else if (EncryptionServiceTypes.PASSWORD_KEYSTORE.equals(config.getType())) {
            return makePasswordKeyStoreService(config, context);
        } else {
            throw new IllegalStateException("Unsupported CryptoConfig type");
        }
    }

    private EncryptionService makePassPhraseService(CryptoConfig config, Context context) {
        return new PassphraseEncryptionServices(context, (PassphraseEncryptionServices.PassPhraseCryptoConfig) config);
    }

    private EncryptionService makePasswordKeyStoreService(CryptoConfig config, Context context) {
        return new PasswordEncryptionServices((PasswordEncryptionServices.PasswordProtectedKeystoreCryptoConfig) config, context);
    }

}
