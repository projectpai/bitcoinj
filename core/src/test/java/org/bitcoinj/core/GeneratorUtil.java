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
  private static final ThreadLocal<byte[]> currentHash160 = new ThreadLocal<>();
  private static final ThreadLocal<byte[]> currentHash256 = new ThreadLocal<>();
  private GeneratorUtil() {
  }


  public static Address byAddressHeader(NetworkParameters params) {
    return new Address(params, params.getAddressHeader(), hash160());
  }

  public static Address byP2SHHeader(NetworkParameters params) {
    return new Address(params, params.getP2SHHeader(), hash160());
  }

  public static byte[] lastHash160() {
    return currentHash160.get();
  }

  public static String deterministicKeySerialized(NetworkParameters params) {
    byte[] bytes = hash656();
    int head = params.getBip32HeaderPub();
    ByteBuffer bb = ByteBuffer.allocate(bytes.length);
    bb.putInt(head);
    bb.put(bytes, 4, bytes.length - 4);

    byte[] newBytes = bb.array();
    byte[] data = Arrays.copyOfRange(newBytes, 0, newBytes.length - 4);
    data[data.length - 33] = 2;
    ByteBuffer nbb = ByteBuffer.allocate(bytes.length);
    nbb.put(data);
    nbb.put(checksum(data));
    return Base58.encode(nbb.array());
  }

  public static String privateKeyBase58(NetworkParameters params) {
    int version = params.getDumpedPrivateKeyHeader();
    Assert.assertTrue(version >=0 && version < 256);
    ByteBuffer bb = ByteBuffer.allocate(36);
    bb.putInt(version);
    bb.put(hash(32));
    byte[] key = Arrays.copyOfRange(bb.array(), 3, 36);

    bb = ByteBuffer.allocate(key.length + 4);
    bb.put(key);
    bb.put(checksum(key));
    return Base58.encode(bb.array());
  }

  private static byte[] checksum(byte[] source) {
    return Arrays.copyOfRange(Sha256Hash.hashTwice(source), 0, 4);
  }

  private static byte[] hash656() {
    return hash(82);
  }

  private static byte[] hash160() {
    currentHash160.set(hash(20));
    return currentHash160.get();
  }

  private static byte[] hash256() {
    currentHash256.set(hash(32));
    return currentHash256.get();
  }

  private static byte[] hash(int length) {
    byte[] bytes = new byte[length];
    sr.nextBytes(bytes);
    return bytes;
  }
}
