package org.uli.ln;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.uli.util.MyOptionBuilder;

public class Ln {

    static private final String NAME = "ln";

    // private boolean fSymbolicLink = false;
    // Taken from http://www.java-only.com/LoadTutorial.javaonly?id=115
    // Please note: The ordering of the parameters is similar to the
    // unix command "ln"
    public void ln(String to, String from, boolean fSymbolicLink) throws IOException {
        Path toPath = FileSystems.getDefault().getPath(to);
        Path fromPath = FileSystems.getDefault().getPath(from);
        if (fSymbolicLink) {
            Files.createSymbolicLink(fromPath, toPath);
        } else {
            Files.createLink(fromPath, toPath);
        }
    }

    public static void main(String[] args) {
        int exitCode = run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    private final static void printHelp(PrintStream out, Options options, ParseException e) {
        if (e != null) {
            out.println(NAME + ": Command line error - " + e.getMessage());
        }
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(NAME, options);
    }

    static public int run(String[] args) {
        Options options = new Options();
        Option h = MyOptionBuilder.init().withLongOpt("help").withDescription("print help").create("h");
        Option s = MyOptionBuilder.init().withLongOpt("symbolic").withDescription("create a symbolic link instead of a hard link").create("s");
        options.addOption(h);
        options.addOption(s);
        int exitCode = 0;
        boolean fHelp = false;
        boolean fSymbolicLink = false;
        Ln ln = new Ln();
        for (;;) {
            CommandLineParser commandLineParser = new PosixParser();
            try {
                CommandLine commandLine = commandLineParser.parse(options, args);
                fHelp = commandLine.hasOption("h");
                if (fHelp) {
                    printHelp(System.out, options, null);
                    break;
                }
                fSymbolicLink = commandLine.hasOption("s");
                String[] remainingArgs = commandLine.getArgs();
                if (remainingArgs.length != 2) {
                    printHelp(System.err, options, null);
                    System.err.println("Expecting 2 command line arguments - got " + remainingArgs.length);
                    exitCode = 11;
                    break;
                }
                int idx = -1;
                String to = remainingArgs[++idx];
                String from = remainingArgs[++idx];
                try {
                    ln.ln(to, from, fSymbolicLink);
                } catch (IOException ioe) {
                    System.err.println(ioe);
                    exitCode = 1;
                }
                return exitCode;
            } catch (ParseException e) {
                System.err.println(NAME + ": Command line error - " + e.getMessage());
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp(NAME, options);
                exitCode = 10;
                break;
            }
        }
        return exitCode;
    }
}
