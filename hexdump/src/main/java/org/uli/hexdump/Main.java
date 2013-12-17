package org.uli.hexdump;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import org.uli.util.HexDump;

public class Main {
    private final HexDump hexDump;

    public String dump(String input) throws IOException {
	File f = new File(input);
	long fl = f.length();
	byte[] bytes = new byte[(int) fl];
	InputStream is = new FileInputStream(f);
	is.read(bytes);
	is.close();
	return hexDump.dump(bytes);
   }

    private Main() {
	this.hexDump = HexDump.builder().dumpHex(true).dumpText(true).bytesPerLine(32).build();
    }

    static public void main(String[] args) throws Exception {
        Main instance = new Main();
        for (String arg : args) {
            System.out.println(instance.dump(arg));
        }
    }
}
