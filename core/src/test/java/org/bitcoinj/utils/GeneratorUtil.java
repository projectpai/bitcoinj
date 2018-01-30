package org.bitcoinj.utils;

import java.security.SecureRandom;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;

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


  public static Address byAddressHeader(NetworkParameters params) {
    return new Address(params, params.getAddressHeader(), hash160());
  }

  public static Address byP2SHHeader(NetworkParameters params) {
    return new Address(params, params.getP2SHHeader(), hash160());
  }

  public static byte[] lastHash() {
    return currentHash.get();
  }

  private static byte[] hash160() {
    byte[] bytes = new byte[20];
    sr.nextBytes(bytes);
    currentHash.set(bytes);
    return bytes;
  }
}
