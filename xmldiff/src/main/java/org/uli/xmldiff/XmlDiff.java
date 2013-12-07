package org.uli.xmldiff;

import java.io.PrintStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.XMLUnit;

import org.uli.util.MyOptionBuilder;

public class XmlDiff {
    static private final String NAME="xmldiff";

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

    private final static Reader getReader(String filename) throws FileNotFoundException {
	File f = new File(filename);
	Reader r = new InputStreamReader(new FileInputStream(f));
	return r;
    }

    // http://www.programcreek.com/2013/08/ignoring-dtd-in-xmlunit/
    private final static void disableXmlValidation() {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setValidating(false);
	try {
	    dbf.setFeature("http://xml.org/sax/features/namespaces", false);
	    dbf.setFeature("http://xml.org/sax/features/validation", false);
	    dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
	    dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
	} catch (ParserConfigurationException e) {
	    e.printStackTrace();
	}
	XMLUnit.setTestDocumentBuilderFactory(dbf);
	XMLUnit.setControlDocumentBuilderFactory(dbf);
    }

    public static int run(String[] args) {
	disableXmlValidation();
	Options options = new Options();
	Option h = MyOptionBuilder.init().withLongOpt("help").withDescription("print help").create("h");
	Option v = MyOptionBuilder.init().withLongOpt("verbose").withDescription("create debug output").create("v");
	options.addOption(h);
	options.addOption(v);
	int exitCode = 0;
	boolean fVerbose = false;
	boolean fHelp = false;
	for (;;) {
	    CommandLineParser commandLineParser = new PosixParser();
	    try {
		CommandLine commandLine = commandLineParser.parse(options, args);
		fVerbose = commandLine.hasOption("v");
		fHelp = commandLine.hasOption("h");
		if (fHelp) {
		    printHelp(System.out, options, null);
		    break;
		}
		String[] remainingArgs = commandLine.getArgs();
		Diff diff = new Diff(getReader(remainingArgs[0]), getReader(remainingArgs[1]));
		if (! diff.similar()) {
		    DetailedDiff details = new DetailedDiff(diff);
		    for (Difference d : (List<Difference>) details.getAllDifferences()) {
			System.out.println(d.toString());
			exitCode=1;
		    }
		}
		break;
	    } catch (ParseException e) {
		System.err.println(NAME + ": Command line error - " + e.getMessage());
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(NAME, options);
		exitCode = 10;
		break;
	    } catch (SAXException e) {
		System.err.println(e);
		exitCode = 10;
		break;
	    } catch (IOException e) {
		System.err.println(e);
		exitCode = 10;
		break;
	    }
	}
	return exitCode;
    }
}
