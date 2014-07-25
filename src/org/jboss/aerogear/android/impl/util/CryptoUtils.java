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
package org.jboss.aerogear.android.impl.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jboss.aerogear.android.impl.crypto.InvalidKeyException;
import org.jboss.aerogear.crypto.CryptoBox;
import org.jboss.aerogear.crypto.keys.PrivateKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CryptoUtils<T> {

    private final CryptoBox cryptoBox;
    private final byte[] IV;
    private final Class<T> modelClass;
    private final Gson gson;

    public CryptoUtils(PrivateKey privateKey, byte[] iv, Class<T> modelClass) {
        this(privateKey, iv, modelClass, new GsonBuilder());
    }

    public CryptoUtils(PrivateKey privateKey, byte[] iv, Class<T> modelClass, GsonBuilder builder) {
        this.modelClass = modelClass;
        this.cryptoBox = new CryptoBox(privateKey);
        this.IV = iv;
        this.gson = builder.create();
    }

    public Collection<T> decrypt(Collection<byte[]> encryptedCollection) {
        List<T> decryptedList = new ArrayList<T>();
        for (byte[] encryptedItem : encryptedCollection) {
            decryptedList.add(decrypt(encryptedItem));
        }
        return decryptedList;
    }

    public byte[] encrypt(T item) {
        String json = gson.toJson(item);
        byte[] message = json.getBytes();
        return cryptoBox.encrypt(IV, message);
    }

    public T decrypt(byte[] data) {
        try {
            byte[] decryptedData = cryptoBox.decrypt(IV, data);
            String json = new String(decryptedData);
            return gson.fromJson(json, modelClass);
        } catch (RuntimeException e) {
            throw new InvalidKeyException(e);
        }
    }

}
