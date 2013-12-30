package org.uli.ln;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class Ln {

    // Taken from http://www.java-only.com/LoadTutorial.javaonly?id=115
    // Please note: The ordering of the parameters is similar to the
    // unix command "ln"
    public void ln(String to, String from) throws IOException {
        Path toPath=FileSystems.getDefault().getPath(to);
        Path fromPath=FileSystems.getDefault().getPath(from);
	Files.createSymbolicLink(fromPath, toPath);
    }

    static public void main(String[] args) throws IOException {
        Ln ln = new Ln();
	int idx = -1;
	String to = args[++idx];
	String from = args[++idx];
	ln.ln(to, from);
    }
}
