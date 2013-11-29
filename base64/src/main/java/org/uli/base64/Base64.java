package org.uli.base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

public class Base64 {

    public String base64(String input) throws IOException {
	File f = new File(input);
	long fl = f.length();
	byte[] bytes = new byte[(int) fl];
	InputStream is = new FileInputStream(f);
	is.read(bytes);
	is.close();
	return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }

    static public void main(String[] args) throws Exception {
        Base64 base64 = new Base64();
        for (String arg : args) {
            System.out.println(base64.base64(arg));
        }
    }
}
