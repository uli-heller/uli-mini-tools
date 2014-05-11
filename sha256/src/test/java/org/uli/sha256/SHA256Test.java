package org.uli.sha256;

import org.junit.Test;

import static org.junit.Assert.*;

public class SHA256Test {
  @Test
  public void testSha256() throws Exception {
      String src = "UliWarDa";
      String sha256 = new SHA256().sha2hex(src);
      assertEquals("ea04b5f95c701e1764e8dca2f0ceefb3a7051c2cae5cdc24ff3dae3703137c8d", sha256);
  }
}
