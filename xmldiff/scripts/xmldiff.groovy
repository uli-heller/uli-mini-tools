import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.DetailedDiff;

def cli = new CliBuilder(usage: "xmldiff [-v][-h] xml1 xml2", posix: true);
cli.with {
    h longOpt:   'help',                                                    'Show usage information'
    v longOpt:   'verbose',                                                 'Create debug output'
};

def options = cli.parse(args);
if (!options) {
  System.err.println "Unable to parse command line options -> EXIT";
  System.exit(1);
}

if (options.h) {
  cli.usage();
  System.exit(0);
}

boolean fVerbose           = options.v;

String[] remainingArgs = options.arguments();
//println remainingArgs.inspect();

Reader getReader(String filename) {
  File f = new File(filename);
  Reader r = new InputStreamReader(new FileInputStream(f));
  return r;
}

Diff diff = new Diff(getReader(remainingArgs[0]), getReader(remainingArgs[1]));

println diff.similar();

if (! diff.similar()) {
  DetailedDiff details = new DetailedDiff(diff);
  for (def d : details.getAllDifferences()) {
    println d;
  }
}
