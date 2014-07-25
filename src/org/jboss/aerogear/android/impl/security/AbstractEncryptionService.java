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
import android.content.SharedPreferences;
import org.jboss.aerogear.android.security.EncryptionService;
import org.jboss.aerogear.crypto.CryptoBox;
import org.jboss.aerogear.crypto.Random;
import org.jboss.aerogear.crypto.encoders.Hex;

/**
 * This class will manage Instance and Application scoped IVs.
 */
public abstract class AbstractEncryptionService implements EncryptionService {

    private static final String APPLICATION_IV_KEY = "applicationIV";
    private static final Random RANDOM = new Random();
    private static final String TAG = AbstractEncryptionService.class.getSimpleName();
    private static final int DEFAULT_IV_LENGHT = 1024;

    protected static final byte[] INSTANCE_IV = RANDOM.randomBytes(DEFAULT_IV_LENGHT);
    protected final byte[] applicationIV;

    public AbstractEncryptionService(Context appContext) {
        synchronized (getClass()) {
            SharedPreferences preferences = appContext.getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
            if (preferences.contains(APPLICATION_IV_KEY)) {
                applicationIV = new Hex().decode(preferences.getString(APPLICATION_IV_KEY, ""));
            } else {
                SharedPreferences.Editor editor = preferences.edit();
                applicationIV = INSTANCE_IV;
                editor.putString(APPLICATION_IV_KEY, new Hex().encode(applicationIV));
                editor.commit();
            }
        }

    }

    protected abstract CryptoBox getCryptoInstance();

    @Override
    public byte[] decrypt(byte[] message) {
        return decrypt(applicationIV, message);
    }

    @Override
    public byte[] decrypt(byte[] iv, byte[] message) {
        return getCryptoInstance().decrypt(iv, message);
    }

    @Override
    public byte[] encrypt(byte[] message) {
        return encrypt(applicationIV, message);
    }

    @Override
    public byte[] encrypt(byte[] iv, byte[] message) {
        return getCryptoInstance().encrypt(iv, message);
    }

}
