package org.uli.sha512;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512Sum {

    public String sha2hex(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
        File f = new File(input);
        long fl = f.length();
        byte[] bytesOfMessage = new byte[(int) fl];
        InputStream is = new FileInputStream(f);
        is.read(bytesOfMessage);
        is.close();
        MessageDigest md = MessageDigest.getInstance("SHA-512");
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
        SHA512Sum sha512 = new SHA512Sum();
        for (String arg : args) {
            System.out.println(sha512.sha2hex(arg));
        }
    }
}
