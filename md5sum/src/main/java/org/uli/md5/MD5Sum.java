package org.uli.md5;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Sum {

    public String md5hex(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
        File f = new File(input);
        long fl = f.length();
        byte[] bytes = new byte[(int) fl];
        InputStream is = new FileInputStream(f);
        is.read(bytes);
        is.close();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytes);
        BigInteger bigInt = new BigInteger(1, thedigest);
        String hashtext = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    static public void main(String[] args) throws Exception {
        MD5Sum md5 = new MD5Sum();
        for (String arg : args) {
            System.out.println(md5.md5hex(arg));
        }
    }
}
