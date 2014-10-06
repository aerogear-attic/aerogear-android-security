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

import java.security.SecureRandom;
import java.util.Arrays;
import static junit.framework.Assert.assertTrue;
import org.jboss.aerogear.android.security.MainActivity;
import org.jboss.aerogear.android.impl.util.PatchedActivityInstrumentationTestCase;

public class PhassphraseKeyServicesTest extends PatchedActivityInstrumentationTestCase<MainActivity> {

    private static byte[] SALT = new SecureRandom().generateSeed(1024);

    public PhassphraseKeyServicesTest() {
        super(MainActivity.class);
    }

    public void testPassphraseKeyServicesEncrypt() {
        PassphraseCryptoConfiguration config = new PassphraseCryptoConfiguration();
        config.setPassphrase("testPhrase");
        config.setSalt(SALT);
        config.setContext(getActivity());
        
        PassphraseEncryptionServices service = new PassphraseEncryptionServices(config);
        String message = "This is a test message";

        byte[] encrypted = service.encrypt(message.getBytes());

        byte[] decrypted = service.decrypt(encrypted);
        assertTrue(Arrays.equals(decrypted, message.getBytes()));

    }

}
