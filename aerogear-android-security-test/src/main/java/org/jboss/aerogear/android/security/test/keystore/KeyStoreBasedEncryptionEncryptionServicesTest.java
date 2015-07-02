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
package org.jboss.aerogear.android.security.test.keystore;

import android.support.test.runner.AndroidJUnit4;
import java.util.Arrays;

import org.jboss.aerogear.android.security.keystore.KeyStoreBasedEncryptionEncryptionServices;
import org.jboss.aerogear.android.security.keystore.KeyStoreBasedEncryptionConfiguration;
import org.jboss.aerogear.android.security.test.MainActivity;
import org.jboss.aerogear.android.security.test.util.PatchedActivityInstrumentationTestCase;
import org.jboss.aerogear.android.security.test.fixture.TestVectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class KeyStoreBasedEncryptionEncryptionServicesTest extends PatchedActivityInstrumentationTestCase {

    public KeyStoreBasedEncryptionEncryptionServicesTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        // Generate the keyStore with the correct password.
        KeyStoreBasedEncryptionConfiguration config = new KeyStoreBasedEncryptionConfiguration();
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        config.setContext(getActivity());

        KeyStoreBasedEncryptionEncryptionServices service = new KeyStoreBasedEncryptionEncryptionServices(config);

    }

    @Test
    public void testPasswordKeyServicesEncrypt() {
        String message = "This is a test message";
        KeyStoreBasedEncryptionConfiguration config = new KeyStoreBasedEncryptionConfiguration();
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        config.setContext(getActivity());

        KeyStoreBasedEncryptionEncryptionServices service = new KeyStoreBasedEncryptionEncryptionServices(config);
        byte[] encrypted = service.encrypt(TestVectors.CRYPTOBOX_IV.getBytes(), message.getBytes());

        Assert.assertFalse(Arrays.equals(encrypted, message.getBytes()));
        byte[] decrypted = service.decrypt(TestVectors.CRYPTOBOX_IV.getBytes(), encrypted);
        Assert.assertTrue(Arrays.equals(decrypted, message.getBytes()));

    }

    @Test
    public void testPasswordKeyServicesEncryptShareKey() {
        KeyStoreBasedEncryptionConfiguration config = new KeyStoreBasedEncryptionConfiguration();
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        config.setContext(getActivity());

        KeyStoreBasedEncryptionEncryptionServices service = new KeyStoreBasedEncryptionEncryptionServices(config);
        KeyStoreBasedEncryptionEncryptionServices service2 = new KeyStoreBasedEncryptionEncryptionServices(config);
        String message = "This is a test message";

        byte[] encrypted = service.encrypt(TestVectors.CRYPTOBOX_IV.getBytes(), message.getBytes());

        Assert.assertFalse(Arrays.equals(encrypted, message.getBytes()));
        byte[] decrypted = service2.decrypt(TestVectors.CRYPTOBOX_IV.getBytes(), encrypted);
        Assert.assertTrue(Arrays.equals(decrypted, message.getBytes()));
    }

}
