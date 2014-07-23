package org.uli.scp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
// import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.uli.util.MyOptionBuilder;

public class Main {

    static private final String NAME = "scp";

    static public void main(String[] args) {
        Main main = new Main();
        System.exit(main.execute(args));
    }

    public int execute(String[] args) {
        int exitCode = 0;
        Options options = new Options();
        Option f = MyOptionBuilder.init().withArgName("fromFile")
                    .hasArg(true)
                    .isRequired(true)
                    .withDescription("input file containing html entities")
                    .create("f");
        Option t = MyOptionBuilder.init().withArgName("toFile")
                    .hasArg(true)
                    .isRequired(true)
                    .withDescription("output file to be generated containing no html entities")
                    .create("t");
        Option encoding = MyOptionBuilder.init().withArgName("encoding")
                    .hasArg(true)
                    .isRequired(false)
                    .withDescription("encoding for input and output")
                    .create("e");
        Option h = MyOptionBuilder.init().hasArg(false)
                    .withDescription("print help (this message)")
                    .create("h");
        options.addOption(f);
        options.addOption(t);
        options.addOption(h);
        options.addOption(encoding);
        CommandLineParser commandLineParser = new PosixParser();
        return exitCode;
    }

    private String readFile(File file, String charsetName) throws IOException {
        byte[] content = new byte[(int) file.length()]; // FIXME: long->int
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(content);
        fileInputStream.close();
        String result = new String(content, charsetName);
        return result;
    }

    private void writeFile(File file, String content, String charsetName) throws UnsupportedEncodingException, IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(content.getBytes(charsetName));
        fileOutputStream.close();
    }
}
