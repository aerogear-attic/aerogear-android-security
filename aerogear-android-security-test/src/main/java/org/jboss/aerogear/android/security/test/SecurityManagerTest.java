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
package org.jboss.aerogear.android.security.test;

import android.support.test.runner.AndroidJUnit4;
import org.jboss.aerogear.android.security.EncryptionService;
import org.jboss.aerogear.android.security.SecurityManager;
import org.jboss.aerogear.android.security.keystore.KeyStoreBasedEncryptionConfiguration;
import org.jboss.aerogear.android.security.keystore.KeyStoreBasedEncryptionEncryptionServices;
import org.jboss.aerogear.android.security.passphrase.PassphraseGeneratedEncryptionConfiguration;
import org.jboss.aerogear.android.security.passphrase.PassphraseGeneratedEncryptionServices;
import org.jboss.aerogear.android.security.test.util.PatchedActivityInstrumentationTestCase;
import org.jboss.aerogear.crypto.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class SecurityManagerTest extends PatchedActivityInstrumentationTestCase {

    public SecurityManagerTest() {
        super(MainActivity.class);
    }

    @Test
    public void testPassPhraseKeyManager() {
        PassphraseGeneratedEncryptionConfiguration config = SecurityManager
                .config("testService", PassphraseGeneratedEncryptionConfiguration.class);
        config.setPassphrase("testPhrase");
        config.setSalt(RandomUtils.randomBytes(1024));
        config.setContext(getActivity());

        EncryptionService service1 = config.asService();
        EncryptionService service2 = SecurityManager.get("testService");
        Assert.assertTrue(service1 instanceof PassphraseGeneratedEncryptionServices);
        Assert.assertSame(service1, service2);

    }

    @Test
    public void testPasswordKeyManager() {
        KeyStoreBasedEncryptionConfiguration config = SecurityManager
                .config("testService", KeyStoreBasedEncryptionConfiguration.class);
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        config.setContext(getActivity());

        EncryptionService service1 = config.asService();
        EncryptionService service2 = SecurityManager.get("testService");

        Assert.assertSame(service1, service2);
        Assert.assertTrue(service1 instanceof KeyStoreBasedEncryptionEncryptionServices);

    }

}
