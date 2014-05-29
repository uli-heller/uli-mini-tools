package org.uli.sha1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.uli.util.FileToByteArray;

public class SHA1Sum {

    public String sha1hex(byte[] bytesOfMessage) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] thedigest = md.digest(bytesOfMessage);
        BigInteger bigInt = new BigInteger(1, thedigest);
        String hashtext = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    static public void main(String[] args) throws Exception {
        SHA1Sum sha1 = new SHA1Sum();
        if (args.length <= 0) {
          FileToByteArray ftba = new FileToByteArray(System.in);
          System.out.println(sha1.sha1hex(ftba.getBytes()) + "  " + ftba.getFilename());
        } else {
          for (String arg : args) {
            FileToByteArray ftba = new FileToByteArray(arg);
            System.out.println(sha1.sha1hex(ftba.getBytes()) + "  " + ftba.getFilename());
          }
        }
    }
}
