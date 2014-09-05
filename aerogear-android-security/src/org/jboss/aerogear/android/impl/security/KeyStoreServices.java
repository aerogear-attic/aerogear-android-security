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
import android.util.Log;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

public class KeyStoreServices {

    private static final String TAG = KeyStoreServices.class.getSimpleName();
    private static final String DEFAULT_KEYSTORE = "default.keystore";
    private char[] password;
    private Context context;

    private KeyStore.PasswordProtection passwordProtectionParameter;
    private KeyStore store;


    public KeyStoreServices(Context context, char[] password) {
        try {
            passwordProtectionParameter = new KeyStore.PasswordProtection(password);
            this.store = KeyStore.getInstance("BKS");
            this.context = context;
            this.store.load(getKeystoreStream(), password);
            this.password = password;
        } catch (KeyStoreException ex) {
            Log.e(TAG, ex.getMessage());
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } catch (CertificateException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

    }

    public byte[] getEntry(String keyAlias) {
        SecretKey key = null;
        try {
            if (store.containsAlias(keyAlias)) {
                KeyStore.SecretKeyEntry keyEntry;
                keyEntry = (KeyStore.SecretKeyEntry) store.getEntry(keyAlias, passwordProtectionParameter);
                key = keyEntry.getSecretKey();
            }
        } catch (NoSuchAlgorithmException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } catch (UnrecoverableEntryException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } catch (KeyStoreException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
        return key != null ? key.getEncoded() : null;
    }

    public void addEntry(String keyAlias, byte[] keyBytes){
        KeyStore.SecretKeyEntry secretEntry = new KeyStore.SecretKeyEntry(new SecretKeySpec(keyBytes, "ECDH"));
        try {
            store.setEntry(keyAlias, secretEntry, passwordProtectionParameter);
        } catch (KeyStoreException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    public void save(){
        try {
            store.store(createKeystoreStream(), password);
        } catch (KeyStoreException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } catch (CertificateException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    private InputStream getKeystoreStream() {
        File keystore = new File(context.getFilesDir(), DEFAULT_KEYSTORE);
        if (!keystore.exists()) {
            return null;
        } else {
            try {
                return new FileInputStream(keystore);
            } catch (FileNotFoundException ex) {
                //This shouldn't happen because we do an explicit check earlier...
                Log.e(TAG, ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
    }

    private FileOutputStream createKeystoreStream() {
        File keystore = new File(context.getFilesDir(), DEFAULT_KEYSTORE);
        if (!keystore.exists()) {
            try {
                return context.openFileOutput(DEFAULT_KEYSTORE, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }
}
