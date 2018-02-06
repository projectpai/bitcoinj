package org.bitcoinj.core;

import java.nio.ByteBuffer;
import java.util.Arrays;
import org.junit.Assert;

/**
 * DataCorrector.
 */
public class DataCorrector {
  private DataCorrector() {}

  public static DumpedPrivateKey correctDumpedPrivateKey(NetworkParameters expected,
      String source, boolean compressed) {
    Assert.assertTrue(validCastToByte(expected.getDumpedPrivateKeyHeader()));

    return new DumpedPrivateKey(expected, essentialSequence(source), compressed);

  }

  public static Address correctAddress(NetworkParameters expected, String source) {
    Assert.assertTrue(validCastToByte(expected.getAddressHeader()));
    return new Address(expected, expected.getAddressHeader(), essentialSequence(source));
  }

  public static Address correctP2SHAddress(NetworkParameters expected, String source) {
    Assert.assertTrue(validCastToByte(expected.getP2SHHeader()));
    return new Address(expected, expected.getP2SHHeader(), essentialSequence(source));
  }

  /**
   *see {@link BitcoinSerializer} for the protocol details
   */
  public static byte[] correctMessage(NetworkParameters expected, byte[] source) {
    byte[] newMagic = ByteBuffer.allocate(4)
        .putInt(expected.getPacketMagic())
        .array();
    return ByteBuffer.allocate(source.length)
        .put(newMagic)
        .put(source, 4, source.length - 4)
        .array();
  }

  private static byte[] essentialSequence(String base58WithChecksum) {
    byte[] bytes = Base58.decodeChecked(base58WithChecksum);
    //cut the version
    return Arrays.copyOfRange(bytes, 1, bytes.length);
  }

  private static boolean validCastToByte(int source) {
    return source >=0 && source < 256;
  }
}
