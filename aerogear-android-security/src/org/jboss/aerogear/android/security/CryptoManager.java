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
package org.jboss.aerogear.android.security;

import android.content.Context;
import org.jboss.aerogear.android.impl.security.DefaultEncryptionServiceFactory;

import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.KeyManager;
import org.jboss.aerogear.android.ConfigurationProvider;
import org.jboss.aerogear.android.impl.security.PassphraseCryptoConfiguration;
import org.jboss.aerogear.android.impl.security.PasswordProtectedKeyStoreCryptoConfiguration;

public class CryptoManager {

    private static Map<String, EncryptionService> services = new HashMap<String, EncryptionService>();

    private static Map<Class<? extends CryptoConfiguration<?>>, ConfigurationProvider<?>> configurationProviderMap = new HashMap<Class<? extends CryptoConfiguration<?>>, ConfigurationProvider<?>>();

    private static OnEncryptionServiceCreatedListener onEncryptionServiceCreatedListener = new OnEncryptionServiceCreatedListener() {
        @Override
        public void onEncryptionServiceCreated(CryptoConfiguration<?> configuration, EncryptionService service) {
            services.put(configuration.getName(), service);
        }
    };

    static {
        PassphraseConfigurationProvider passphraseCryptoConfigProvider = new PassphraseConfigurationProvider();
        CryptoManager.registerConfigurationProvider(PassphraseCryptoConfiguration.class, passphraseCryptoConfigProvider);
        PasswordProtectedKeystoreCryptoConfigurationProvider digestConfigurationProvider = new PasswordProtectedKeystoreCryptoConfigurationProvider();
        CryptoManager.registerConfigurationProvider(PasswordProtectedKeyStoreCryptoConfiguration.class, digestConfigurationProvider);
    }

    private CryptoManager() {
    }

    
    /**
     * 
     * This will add a new Configuration that this Manager can build
     * Configurations for.
     * 
     * @param <CFG> the actual Configuration type
     * @param configurationClass the class of configuration to be registered
     * @param provider the instance which will provide the configuration.
     */
    public static <CFG extends CryptoConfiguration<CFG>> void registerConfigurationProvider
            (Class<CFG> configurationClass, ConfigurationProvider<CFG> provider) {
        configurationProviderMap.put(configurationClass, provider);
    }

    /**
     * Begins a new fluent configuration stanza.
     * 
     * @param <CFG> the Configuration type.
     * @param name an identifier which will be used to fetch the AuthenticationModule after
     *            configuration is finished.
     * @param authenticationConfigurationClass the class of the configuration type.
     * 
     * @return a AuthenticationConfiguration which can be used to build a AuthenticationModule object.
     */
    public static <CFG extends CryptoConfiguration<CFG>> CFG config(String name, Class<CFG> authenticationConfigurationClass) {

        @SuppressWarnings("unchecked")
        ConfigurationProvider<? extends CryptoConfiguration<CFG>> provider =
                (ConfigurationProvider<? extends CryptoConfiguration<CFG>>)
                configurationProviderMap.get(authenticationConfigurationClass);

        if (provider == null) {
            throw new IllegalArgumentException("Configuration not registered!");
        }

        return provider.newConfiguration()
                .setName(name)
                .addOnEncryptionServiceCreatedListener(onEncryptionServiceCreatedListener);

    }

    /**
     * Fetches an instance of encryption service.
     * 
     * @param name the name provided to {@link KeyManager#encryptionService(String, org.jboss.aerogear.android.security.CryptoConfig, android.content.Context)  }
     * @return a cached encryption service or null.
     */
    public static EncryptionService get(String name) {
        return services.get(name);
    }


}
