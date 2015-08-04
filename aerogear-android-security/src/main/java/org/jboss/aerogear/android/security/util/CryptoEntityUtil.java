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
package org.jboss.aerogear.android.security.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jboss.aerogear.android.security.EncryptionService;
import org.jboss.aerogear.android.security.InvalidKeyException;

public class CryptoEntityUtil<T> {

    private final EncryptionService encryptionService;
    private final byte[] IV;
    private final Class<T> modelClass;
    private final Gson gson;

    public CryptoEntityUtil(EncryptionService encryptionService, byte[] iv, Class<T> modelClass) {
        this(encryptionService, iv, modelClass, new GsonBuilder());
    }

    public CryptoEntityUtil(EncryptionService encryptionService, byte[] iv, Class<T> modelClass, GsonBuilder builder) {
        this.encryptionService = encryptionService;
        this.IV = iv;
        this.modelClass = modelClass;
        this.gson = builder.create();
    }

    public byte[] encrypt(T item) {
        String json = gson.toJson(item);
        byte[] message = json.getBytes();
        return encryptionService.encrypt(IV, message);
    }

    public T decrypt(byte[] data) {
        try {
            byte[] decryptedData = encryptionService.decrypt(IV, data);
            String json = new String(decryptedData);
            return gson.fromJson(json, modelClass);
        } catch (RuntimeException e) {
            throw new InvalidKeyException(e);
        }
    }

}
