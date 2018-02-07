/*
 * Copyright 2013 Matija Mazi.
 * Copyright 2014 Andreas Schildbach
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

package org.bitcoinj.crypto;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.bitcoinj.core.Utils.HEX;
import static org.junit.Assert.assertEquals;

/**
 * A test with test vectors as per BIP 32 spec: https://github.com/bitcoin/bips/blob/master/bip-0032.mediawiki#Test_Vectors
 */
@RunWith(Parameterized.class)
public class BIP32Test {
    private static final Logger log = LoggerFactory.getLogger(BIP32Test.class);

    @Parameterized.Parameters
    public static Collection<HDWTestVector> vectors() {
      return Arrays.asList(
            new HDWTestVector(
                    "000102030405060708090a0b0c0d0e0f",
                    "paiv7CLsatDqdaCRhYkYcLaRJw8kK4KT8CJvtgKUiZoRYPycRK8UGamMWniBfmvJsbC2ZrHCFZewWpbCS34MqSH9p8SfdXpM2LGBBeUyDMdQvd4",
                    "paip6Nt6Wry8bUrjUJgwaB5mfZAobHkoMeic9HwtM3EZP6pG6QJLkjX8wc821h4BidygTyxU22iDwqvkcQdNeJ27i7fMkERLSixhWiaXQniWwPQ",
                    Arrays.asList(
                            new HDWTestVector.DerivedTestCase(
                                    "Test m/0H",
                                    new ChildNumber[]{new ChildNumber(0, true)},
                                    "paiv7FthqQbCP2dg71MYJA9vDfnDYoPjUgUURTjNTEfte5xgZccNZxCbsGfNxBK74nsgJCbbuSBnnDnAiuDmznH3Y3aPhUREx5GJAdPTRf1aNbC",
                                    "paip6SRvmPLVLwHysmHwFzfGaHpGq2q5i8t9g5Mn5i72UnoLEhnF46xPJ65DJ6EXzbUWkXNkmDJ3zt2dwx9aTyka6JhY1k77vVgZKYzQcXkw1oT"
                            ),
                            new HDWTestVector.DerivedTestCase(
                                    "Test m/0H/1",
                                    new ChildNumber[]{new ChildNumber(0, true), new ChildNumber(1, false)},
                                    "paiv7Hf9LWyry5nZDAJM7yi4atsTFEguhHuGDBvKVqHZMhuXtyST4vrQZ4SLY5Ab65njmhntLyfPkGaW1UPfmEphVwer7wDC8umdGwvXLyedDLi",
                                    "paip6UCNGVj9vzSryvEk5pDQwWuWXU8FvkJwToYj8JihCQkBa4cKZ5cBysrAszDpV1E8J2CswGp3HNwpPtprQ9aehDZUPrRscFVVhiUUazt2qMo"
                            ),
                            new HDWTestVector.DerivedTestCase(
                                    "Test m/0H/1/2H",
                                    new ChildNumber[]{new ChildNumber(0, true), new ChildNumber(1, false), new ChildNumber(2, true)},
                                    "paiv7J5RWLghkwTNJpNqNSMAYYSkKs5VXsQ4xSEMkhLGAn1sSz2HDD4JkjQJoeKALRnWwsqLNwEiaaKuDKwjNsBXysJPMMQ3wutDsDg1zB99t83",
                                    "paip6UceSKRzir7g5aKELGrWuAUoc6WqmKokD3rmPAmQ1UrX85C9hMp6BYp99ZV5HL7nEpEupDtmpT3VvRAUAC5aoEBxE1L9tPayDnMKitLPe6p"
                            ),
                            new HDWTestVector.DerivedTestCase(
                                    "Test m/0H/1/2H/2",
                                    new ChildNumber[]{new ChildNumber(0, true), new ChildNumber(1, false), new ChildNumber(2, true), new ChildNumber(2, false)},
                                    "paiv7L4msKUhLxhmiZQxuQZDosKZm3uxyzk3W2Vr7ozrDBhWa2aJWjeqwCJ7ALN98S9puWV7d7xNafYh5os8gJVVMGZRgNuvc5oyYM57VmjD7n2",
                                    "paip6WbzoJDzJsN5VKMMsF4aAVMd3HMKDT9ike8FkHRz3tYAF7kAztQdN1hwWHra3jj9tTuSVawiCwhdivKUppFBBp1DSYqHmZfxeEJZqWBC2Zx"
                            ),
                            new HDWTestVector.DerivedTestCase(
                                    "Test m/0H/1/2H/2/1000000000",
                                    new ChildNumber[]{new ChildNumber(0, true), new ChildNumber(1, false), new ChildNumber(2, true), new ChildNumber(2, false), new ChildNumber(1000000000, false)},
                                    "paiv7MkdoL5MWtuYMhML2DQ8znyGqav7DY2psiA7dGcnCsiLoDUEHVRXwhxQm9qNRQoS8HiZwHzgsRUU4F76sVwo9FEgNDUnMV7vfnkqns2eytj",
                                    "paip6YHrjJpeUoZr8THiz3uVMR1L7pMTSzSW8KnXFk3v3aYzUJe6meBKNXNF75F6Vf53rXCkGXPcRxEdPTKwVQh14ikgcj1VyZqNfVCnb2gnJvT"
                            )
                    )
            ),
            new HDWTestVector(
                    "fffcf9f6f3f0edeae7e4e1dedbd8d5d2cfccc9c6c3c0bdbab7b4b1aeaba8a5a29f9c999693908d8a8784817e7b7875726f6c696663605d5a5754514e4b484542",
                    "paiv7CLsatDqdaCRfTT6oEt9b6ZHtbdgEsZdQNPRmWBAJ5t6zx5SSQng13QXRU6GkNfaMAqRfAGV51xeBrnq7QWGCezx8oCSuA2W8Jy3tApQKnS",
                    "paip6Nt6Wry8bUrjSDPVm5PVwibMAq52UKyJez1qPycJ8nikg3FJvZYTRrpMmNNVkUTwAz59zzyFT34oM4VBRhBMaEBtnZndxUiKhKotKzGXieS",
                    Arrays.asList(
                            new HDWTestVector.DerivedTestCase(
                                    "Test m/0",
                                    new ChildNumber[]{new ChildNumber(0, false)},
                                    "paiv7FBjaAoRLNqkovc8kGQz8mcQ2fHZr1SwFkDLUGNj9D75tcbAggMAuoPhWBPGXSqnkxvs6LwGc5ASzPkZxKBGaXvGLdrGcnErYDC3s6gdWL3",
                                    "paip6RixW9YiJHW4agYXi6vLVPeTJtiv5TrcWMqk6joryuwjZhm3Aq6xLcoXr8ZGES7cDNaT2qtrr7v3rr68DzPz6FFJ2gme5FQBi9KJzpxwiPF"
                            ),
                            new HDWTestVector.DerivedTestCase(
                                    "Test m/0/2147483647H",
                                    new ChildNumber[]{new ChildNumber(0, false), new ChildNumber(2147483647, true)},
                                    "paiv7HD5KkhLHYjN2Pe2Jr9LQ9WBjnPRWF9gxmamE4fETygCFPvFDsaQ9uAe236h17wHz7Lsia12UfnxpwEZdfUYp2bzKv2X1SUUTqv3rck5wS9",
                                    "paip6TkJFjSdFTPfo9aRGgegkmYF21pmjhZNDPDArY6NJgWqvV67i2LBaiaUMzuCDhhby8gh7jfDD31NofjZfBqKiHZeCfMkGhMkM77LxWV9qnF"
                            ),
                            new HDWTestVector.DerivedTestCase(
                                    "Test m/0/2147483647H/1",
                                    new ChildNumber[]{new ChildNumber(0, false), new ChildNumber(2147483647, true), new ChildNumber(1, false)},
                                    "paiv7KSeA48wR43irFJqGauCH25iR9ARfevaLNxSavMtdNwmTmmhcGbR87MtcBL1Mntqwu5d4AqY7SSFsW8q8WcYbDxyCDej54a5f3zRP8a252J",
                                    "paip6Vys62tENxi2d1FEERQYde7mhNbmu7LFazarDPo2U5nR8rwa6RMCYvmix7egugWg3jV8jm73RgjzQCuhJRypHkos8vrLcEuTvi2fgj6TnXP"
                            ),
                            new HDWTestVector.DerivedTestCase(
                                    "Test m/0/2147483647H/1/2147483646H",
                                    new ChildNumber[]{new ChildNumber(0, false), new ChildNumber(2147483647, true), new ChildNumber(1, false), new ChildNumber(2147483646, true)},
                                    "paiv7MAhp433grAmisGkWgieHBZgDZu4HHv9ydpTTecHa2nojLfNyuSpscdY8muU5hJr1BayZzULSF1XX9FxMFo8aZLbXgYzS57Wk99DdG3ygmQ",
                                    "paip6Xhvk2nLekq5VdD9UXDzdobjVoLQWkKqEFSs683RQjdTQRqFU4CcJS3NUjxAPdBy462QtbSvMdvrYUx1VYGjoUyi1Bk6u1w1nyR6stfeNeu"
                            ),
                            new HDWTestVector.DerivedTestCase(
                                    "Test m/0/2147483647H/1/2147483646H/2",
                                    new ChildNumber[]{new ChildNumber(0, false), new ChildNumber(2147483647, true), new ChildNumber(1, false), new ChildNumber(2147483646, true), new ChildNumber(2, false)},
                                    "paiv7N3QrPezEcgvdFLAMN7j1PbZ73Ba4atcgC8gmhERBuLjKfthp7g6ehXRafzL4HH9SfxttXjd5cvf8fZgQBdyhjyApMkEexeCBhuYjwvLFCe",
                                    "paip6YadnNQHCXMEQ1GZKCd5N1dcPGcvJ3JHvom6QAfZ2cBNzm4aJGRt5WwFveUXv9mon2LJDaaPf1xPayHKhJ1AMi7cC7pxXVBQu7KP4wgWWyf"
                            )
                    )
            ),
            new HDWTestVector(
                    "4b381541583be4423346c643850da4b320e46a87ae3d2a4e6da11eba819cd4acba45d239319ac14f863b8d5ab5a0d0c64d2e8a1e7d1457df2e5a3c51c73235be",
                    "paiv7CLsatDqdaCRhyXFNVSfLxXTZFKD5L6LX6NpYtmgK5xBAoiUfSh8hKZycTfsuiPgFvb3L8rdAqWbqbWDbBq2Y2umEboKnMHUTBcT4CLC5SA",
                    "paip6Nt6Wry8bUrjUjTeLKx1haZWqUkZJnW1mi1EBNCp9nnpqttM9bSv88yoxQqJA7YvVCSH7gP3dRYARLHSVH3Z4M6oAne5j1SP4S5zLwzGqGX",
                Collections.singletonList(
                    new HDWTestVector.DerivedTestCase(
                        "Test m/0H",
                        new ChildNumber[]{new ChildNumber(0, true)},
                    "paiv7F9SjnP1CkV5T7CGJDnpxtTotfrBHmEhoNU7MRH2YVKAme8CpdnDCTHcq4oakLm5Qg68ZU1KYWKARdkvoM28ToZqE8kRELTGYq8b1MLSEoi",
                    "paip6Rgffm8JAf9PDs8fG4JBKWVsAuHXXDeP3z6WytiAPC9pSjJ5JnXzdGhTB1pscU8KiW9JteqAapWZXTPn9TYUG1UYeH7DRcNjrurFV9jhyy5"))
            )
      );
    }

    private final HDWTestVector tv;
    private final NetworkParameters params = MainNetParams.get();

    public BIP32Test(HDWTestVector tv) {
      this.tv = tv;
    }

    @Test
    public void testVector() {
        DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(HEX.decode(tv.seed));
        assertEquals(
            testEncode(tv.priv),
            testEncode(masterPrivateKey.serializePrivB58(params)));
        assertEquals(
            testEncode(tv.pub),
            testEncode(masterPrivateKey.serializePubB58(params)));
        DeterministicHierarchy dh = new DeterministicHierarchy(masterPrivateKey);
        for (int i = 0; i < tv.derived.size(); i++) {
            HDWTestVector.DerivedTestCase tc = tv.derived.get(i);
            log.info("{}", tc.name);
            assertEquals(tc.name, String.format(Locale.US, "Test %s", tc.getPathDescription()));
            int depth = tc.path.length - 1;
            DeterministicKey ehkey = dh.deriveChild(Arrays.asList(tc.path).subList(0, depth), false, true, tc.path[depth]);
            assertEquals(testEncode(tc.priv), testEncode(ehkey.serializePrivB58(params)));
            assertEquals(testEncode(tc.pub), testEncode(ehkey.serializePubB58(params)));
        }
    }

    private String testEncode(String what) {
        return HEX.encode(Base58.decodeChecked(what));
    }

    static class HDWTestVector {
        final String seed;
        final String priv;
        final String pub;
        final List<DerivedTestCase> derived;

        HDWTestVector(String seed, String priv, String pub, List<DerivedTestCase> derived) {
            this.seed = seed;
            this.priv = priv;
            this.pub = pub;
            this.derived = derived;
        }

        static class DerivedTestCase {
            final String name;
            final ChildNumber[] path;
            final String pub;
            final String priv;

            DerivedTestCase(String name, ChildNumber[] path, String priv, String pub) {
                this.name = name;
                this.path = path;
                this.pub = pub;
                this.priv = priv;
            }

            String getPathDescription() {
                return "m/" + Joiner.on("/").join(Iterables.transform(Arrays.asList(path), Functions.toStringFunction()));
            }
        }
    }
}
