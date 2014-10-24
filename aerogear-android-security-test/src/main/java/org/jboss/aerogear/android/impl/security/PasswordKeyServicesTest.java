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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jboss.aerogear.android.impl.security;

import java.util.Arrays;
import org.jboss.aerogear.android.security.MainActivity;
import org.jboss.aerogear.android.impl.util.PatchedActivityInstrumentationTestCase;
import org.jboss.aerogear.fixture.TestVectors;

public class PasswordKeyServicesTest extends PatchedActivityInstrumentationTestCase<MainActivity> {

    public PasswordKeyServicesTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Generate the keyStore with the correct password.
        PasswordProtectedKeyStoreCryptoConfiguration config = new PasswordProtectedKeyStoreCryptoConfiguration();
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        config.setContext(getActivity());

        PasswordEncryptionServices service = new PasswordEncryptionServices(config);

    }

    public void testPasswordKeyServicesEncrypt() {
        String message = "This is a test message";
        PasswordProtectedKeyStoreCryptoConfiguration config = new PasswordProtectedKeyStoreCryptoConfiguration();
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        config.setContext(getActivity());

        PasswordEncryptionServices service = new PasswordEncryptionServices(config);
        byte[] encrypted = service.encrypt(TestVectors.CRYPTOBOX_IV.getBytes(), message.getBytes());

        assertFalse(Arrays.equals(encrypted, message.getBytes()));
        byte[] decrypted = service.decrypt(TestVectors.CRYPTOBOX_IV.getBytes(), encrypted);
        assertTrue(Arrays.equals(decrypted, message.getBytes()));

    }

    public void testPasswordKeyServicesEncryptShareKey() {
        PasswordProtectedKeyStoreCryptoConfiguration config = new PasswordProtectedKeyStoreCryptoConfiguration();
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        config.setContext(getActivity());

        PasswordEncryptionServices service = new PasswordEncryptionServices(config);
        PasswordEncryptionServices service2 = new PasswordEncryptionServices(config);
        String message = "This is a test message";

        byte[] encrypted = service.encrypt(TestVectors.CRYPTOBOX_IV.getBytes(), message.getBytes());

        assertFalse(Arrays.equals(encrypted, message.getBytes()));
        byte[] decrypted = service2.decrypt(TestVectors.CRYPTOBOX_IV.getBytes(), encrypted);
        assertTrue(Arrays.equals(decrypted, message.getBytes()));
    }

}
