package org.bitcoinj.core;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import org.junit.Assert;

/**
 * GeneratorUtil.
 */
public class GeneratorUtil {
  private static final SecureRandom sr = new SecureRandom();
  {
    sr.setSeed(1782356L);
  }
  private static final ThreadLocal<byte[]> currentHash = new ThreadLocal<>();
  private GeneratorUtil() {
  }


  public static Address addressByHeader(NetworkParameters params) {
    return new Address(params, params.getAddressHeader(), hash160());
  }

  public static Address addressByP2SHHeader(NetworkParameters params) {
    return new Address(params, params.getP2SHHeader(), hash160());
  }

  public static Transaction transaction(NetworkParameters params) {
    Transaction t = new Transaction(params);
    t.addOutput(Coin.COIN, addressByHeader(params));
    return t;
  }

  public static byte[] lastHash() {
    return currentHash.get();
  }

  public static byte[] withChecksum4B(byte[] source) {
    ByteBuffer bb = ByteBuffer.allocate(source.length + 4);
    bb.put(source);
    bb.put(checksum4B(source));
    currentHash.set(bb.array());
    return currentHash.get();
  }

  public static String privateKeyBase58(NetworkParameters params) {
    int version = params.getDumpedPrivateKeyHeader();
    Assert.assertTrue(version >= 0 && version < 256);
    ByteBuffer bb = ByteBuffer.allocate(33);
    bb.put((byte) version);
    bb.put(hash(32));

    return Base58.encode(withChecksum4B(bb.array()));
  }

  public static VersionedChecksummedBytes bytesByParams(NetworkParameters params) {
    int version = params.getAddressHeader();
    Assert.assertTrue(version >= 0 && version < 256);
    return new VersionedChecksummedBytes(version, hash160());
  }

  private static byte[] checksum4B(byte[] source) {
    return Arrays.copyOfRange(Sha256Hash.hashTwice(source), 0, 4);
  }

  private static byte[] hash160() {
    currentHash.set(hash(20));
    return currentHash.get();
  }

  private static byte[] hash(int length) {
    byte[] bytes = new byte[length];
    sr.nextBytes(bytes);
    return bytes;
  }
}
