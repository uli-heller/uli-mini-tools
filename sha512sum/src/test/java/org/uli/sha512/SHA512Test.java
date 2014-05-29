package org.uli.sha512;

import org.junit.Test;

import static org.junit.Assert.*;

public class SHA512Test {
  @Test
  public void testSha512() throws Exception {
      String src = "UliWarDa";
      String sha512 = new SHA512().sha2hex(src);
      assertEquals("6cfb98c7297cd97dabeb557deb50f72e83e9fe8a49b18cd5819d6e0cfe51397293c9fabf7341e7f7c19b32a6812d6ac67c29b3ce2c7e77ac94f7362df41a8808", sha512);
  }
}
