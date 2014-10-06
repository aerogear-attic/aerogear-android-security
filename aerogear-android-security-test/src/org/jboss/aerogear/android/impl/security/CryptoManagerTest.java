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
import org.jboss.aerogear.android.security.MainActivity;
import org.jboss.aerogear.android.impl.util.PatchedActivityInstrumentationTestCase;
import org.jboss.aerogear.android.security.EncryptionService;
import org.jboss.aerogear.android.security.CryptoManager;
import org.jboss.aerogear.crypto.RandomUtils;


import static org.mockito.Mockito.mock;

public class CryptoManagerTest extends PatchedActivityInstrumentationTestCase<MainActivity> {

    public CryptoManagerTest() {
        super(MainActivity.class);
    }

    public void testPassPhraseKeyManager() {
        PassphraseCryptoConfiguration config = CryptoManager.config("testService", PassphraseCryptoConfiguration.class);
        config.setPassphrase("testPhrase");
        config.setSalt(RandomUtils.randomBytes(1024));
        config.setContext(getActivity());
        
        EncryptionService service1 = config.asService();
        EncryptionService service2 = CryptoManager.get("testService");
        assertTrue(service1 instanceof PassphraseEncryptionServices);
        assertSame(service1, service2);


    }

    public void testPasswordKeyManager() {
        PasswordProtectedKeyStoreCryptoConfiguration config = CryptoManager.config("testService", PasswordProtectedKeyStoreCryptoConfiguration.class);
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        config.setContext(getActivity());
        
        EncryptionService service1 = config.asService();
        EncryptionService service2 = CryptoManager.get("testService");

        assertSame(service1, service2);
        assertTrue(service1 instanceof PasswordEncryptionServices);
        

    }

}
