/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android.security.passphrase;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.SecureRandom;
import java.util.Arrays;

import static android.support.test.InstrumentationRegistry.getContext;

@RunWith(AndroidJUnit4.class)
public class PassphraseGeneratedEncryptionServicesTest {

    private static byte[] SALT = new SecureRandom().generateSeed(1024);

    @Test
    public void testPassphraseKeyServicesEncrypt() {
        PassphraseGeneratedEncryptionConfiguration config =
                new PassphraseGeneratedEncryptionConfiguration();
        config.setPassphrase("testPhrase");
        config.setSalt(SALT);
        config.setContext(getContext());

        PassphraseGeneratedEncryptionServices service =
                new PassphraseGeneratedEncryptionServices(config);
        String message = "This is a test message";

        byte[] encrypted = service.encrypt(message.getBytes());

        byte[] decrypted = service.decrypt(encrypted);
        Assert.assertTrue(Arrays.equals(decrypted, message.getBytes()));

    }

}
