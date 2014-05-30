package org.uli.base64;

import java.io.IOException;

import org.uli.util.FileToByteArray;

public class Base64 {

    public String base64(byte[] bytes) throws IOException {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }

    static public void main(String[] args) throws Exception {
        Base64 base64 = new Base64();
        if (args.length <= 0) {
          FileToByteArray ftba = new FileToByteArray(System.in);
          System.out.println(base64.base64(ftba.getBytes()));
        } else {
          boolean fPrintSuffix = false;
          if (args.length > 1) {
              fPrintSuffix=true;
          }
          for (String arg : args) {
            FileToByteArray ftba = new FileToByteArray(arg);
            System.out.println(base64.base64(ftba.getBytes())+(fPrintSuffix ? "  "+ftba.getFilename() : ""));
          }
        }
    }
}
