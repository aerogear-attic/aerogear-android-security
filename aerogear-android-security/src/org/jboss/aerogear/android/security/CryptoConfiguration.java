/*
 * Copyright 2014 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android.security;

import android.content.Context;
import java.util.Collection;
import java.util.HashSet;
import org.jboss.aerogear.android.Config;

/**
 *
 * @param <CONFIGURATION> The concrete implementation of the CryptoConfiguration
 */
public abstract class CryptoConfiguration<CONFIGURATION extends CryptoConfiguration<CONFIGURATION>> implements Config<CONFIGURATION> {

    private String name;
    private Context context;

    private Collection<OnEncryptionServiceCreatedListener> listeners = new HashSet<OnEncryptionServiceCreatedListener>();

    public Context getContext() {
        return context;
    }

    public CONFIGURATION setContext(Context context) {
        this.context = context;
        return (CONFIGURATION) this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CONFIGURATION setName(String name) {
        this.name = name;
        return (CONFIGURATION) this;
    }

    /**
     *
     * Creates a {@link  EncryptionService} based on the current configuration
     * and notifies all listeners
     *
     * @return An {@link  EncryptionService} based on this configuration
     * @throws IllegalStateException if context is null
     *
     */
    public final EncryptionService asService() {

        if (getContext() == null) {
            throw new IllegalStateException("An applicationContext must be provided");
        }
        
        EncryptionService newService = buildService();
        for (OnEncryptionServiceCreatedListener listener : getOnEncryptionServiceCreatedListeners()) {
            listener.onEncryptionServiceCreated(this, newService);
        }
        return newService;
    }

    /**
     *
     * Validates configuration parameters and returns a
     * {@link  EncryptionService} instance.
     *
     * @return An {@link  EncryptionService} based on this configuration
     *
     */
    protected abstract EncryptionService buildService();

    /**
     * OnEncryptionServiceCreatedListeners are a collection of classes to be
     * notified when the configuration of the service is complete.
     *
     * @return the current collection.
     */
    public Collection<OnEncryptionServiceCreatedListener> getOnEncryptionServiceCreatedListeners() {
        return listeners;
    }

    /**
     * OnEncryptionServiceCreatedListeners are a collection of classes to be
     * notified when the configuration of the service is complete.
     *
     * @param listener new listener to add to the collection
     * @return this configuration
     */
    public CONFIGURATION addOnEncryptionServiceCreatedListener(OnEncryptionServiceCreatedListener listener) {
        this.listeners.add(listener);
        return (CONFIGURATION) this;
    }

    /**
     * OnEncryptionServiceCreatedListeners are a collection of classes to be
     * notified when the configuration of the Service is complete.
     *
     * @param listeners new collection to replace the current one
     * @return this configuration
     */
    public CONFIGURATION setOnEncryptionServiceCreatedListeners(Collection<OnEncryptionServiceCreatedListener> listeners) {
        listeners.addAll(listeners);
        return (CONFIGURATION) this;
    }

}
