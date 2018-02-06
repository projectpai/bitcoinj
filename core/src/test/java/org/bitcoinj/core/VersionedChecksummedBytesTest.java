/*
 * Copyright 2014 bitcoinj project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.core;

import java.nio.ByteBuffer;
import java.util.Collection;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 *
 */
@RunWith(Parameterized.class)
public class VersionedChecksummedBytesTest {

    public VersionedChecksummedBytesTest(NetworkParameters params) {
        this.params = params;
    }

    @Parameterized.Parameters
    public static Collection<NetworkParameters> networks() {
        return asList(TestNet3Params.get(),
            MainNetParams.get());
    }

    private final NetworkParameters params;

    @Test
    public void stringification() throws Exception {
        // Test a testnet address.

        VersionedChecksummedBytes val = GeneratorUtil.bytesByParams(params);
        byte[] lastHash = GeneratorUtil.lastHash();
        ByteBuffer bb = ByteBuffer.allocate(lastHash.length + 1);
        bb.put((byte) params.addressHeader);
        bb.put(lastHash);

        String base58 = Base58.encode(GeneratorUtil.withChecksum4B(bb.array()));
        assertEquals(base58, val.toString());
    }

    @Test
    public void cloning() throws Exception {
        VersionedChecksummedBytes a = GeneratorUtil.bytesByParams(params);
        VersionedChecksummedBytes b = a.clone();

        assertEquals(a, b);
        assertNotSame(a, b);
    }

    @Test
    public void comparisonCloneEqualTo() throws Exception {
        VersionedChecksummedBytes a = GeneratorUtil.bytesByParams(params);
        VersionedChecksummedBytes b = a.clone();

        assertEquals(0, a.compareTo(b));
        assertEquals(0, b.compareTo(a));
    }
}
