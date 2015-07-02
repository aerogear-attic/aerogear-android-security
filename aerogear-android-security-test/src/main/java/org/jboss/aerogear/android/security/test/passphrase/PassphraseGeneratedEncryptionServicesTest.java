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
package org.jboss.aerogear.android.security.test.passphrase;

import android.support.test.runner.AndroidJUnit4;
import org.jboss.aerogear.android.security.passphrase.PassphraseGeneratedEncryptionConfiguration;
import org.jboss.aerogear.android.security.passphrase.PassphraseGeneratedEncryptionServices;
import org.jboss.aerogear.android.security.test.MainActivity;
import org.jboss.aerogear.android.security.test.util.PatchedActivityInstrumentationTestCase;

import java.security.SecureRandom;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PassphraseGeneratedEncryptionServicesTest extends PatchedActivityInstrumentationTestCase {

    private static byte[] SALT = new SecureRandom().generateSeed(1024);

    public PassphraseGeneratedEncryptionServicesTest() {
        super(MainActivity.class);
    }

    @Test
    public void testPassphraseKeyServicesEncrypt() {
        PassphraseGeneratedEncryptionConfiguration config = new PassphraseGeneratedEncryptionConfiguration();
        config.setPassphrase("testPhrase");
        config.setSalt(SALT);
        config.setContext(getActivity());

        PassphraseGeneratedEncryptionServices service = new PassphraseGeneratedEncryptionServices(config);
        String message = "This is a test message";

        byte[] encrypted = service.encrypt(message.getBytes());

        byte[] decrypted = service.decrypt(encrypted);
        Assert.assertTrue(Arrays.equals(decrypted, message.getBytes()));

    }

}
