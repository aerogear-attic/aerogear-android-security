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

/**
 * Classes which implement this interface are responsible for creating 
 * Encryption services from a config object.
 */
public interface EncryptionServiceFactory {

    /**
     * 
     * This method provides a EncryptionService.  Services MAY be cached and 
     * reused or they may be new objects every time.
     * 
     * @param config config objects may provide any parameters they wish.
     * @param context the Android Application Context
     * @return A valid Encryption Service
     */
    public EncryptionService getService(CryptoConfig config, Context context);

}
