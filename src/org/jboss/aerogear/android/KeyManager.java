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
package org.jboss.aerogear.android;

import android.content.Context;
import org.jboss.aerogear.android.impl.security.DefaultEncryptionServiceFactory;
import org.jboss.aerogear.android.security.CryptoConfig;
import org.jboss.aerogear.android.security.EncryptionService;
import org.jboss.aerogear.android.security.EncryptionServiceFactory;

import java.util.HashMap;
import java.util.Map;

public class KeyManager {

    private final EncryptionServiceFactory serviceFactory;

    private final Map<String, EncryptionService> services = new HashMap<String, EncryptionService>();

    public KeyManager(EncryptionServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public KeyManager() {
        serviceFactory = new DefaultEncryptionServiceFactory();
    }

    /**
     * 
     * This method will create a encryptionService based on the config object.
     * Instances of this service will be accessible using the KeyManager#get(String name).
     * 
     * 
     * @param name the name to cache the generated instance by.
     * @param config the config object.  
     * @param context The Android Application Context.
     * @return a encryption service.
     */
    public EncryptionService encryptionService(String name, CryptoConfig config, Context context) {
        EncryptionService service = serviceFactory.getService(config, context);
        services.put(name, service);
        return service;
    }

    /**
     * Fetches an instance of encryption service.
     * 
     * @param name the name provided to {@link KeyManager#encryptionService(String, org.jboss.aerogear.android.security.CryptoConfig, android.content.Context)  }
     * @return a cached encryption service or null.
     */
    public EncryptionService get(String name) {
        return services.get(name);
    }

    /**
     * Removes an instance of encryption service.
     *
     * @param name the name provided to {@link KeyManager#encryptionService(String, org.jboss.aerogear.android.security.CryptoConfig, android.content.Context) }
     */
    public void remove(String name) {
        services.remove(name);
    }

}
