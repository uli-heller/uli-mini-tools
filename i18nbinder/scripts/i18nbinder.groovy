//import groovy.transform.EqualsAndHashCode;
//import groovy.transform.ToString;

String DEFAULT_DIRNAME  = ".";
String DEFAULT_ENCODING = System.getProperty("file.encoding");
String DEFAULT_PATTERN  = "**/*.properties";

def cli = new CliBuilder(usage: "i18nbinder [-v][-h] [-u] [-n] [-c [-d dirName] [-p pattern]] [-e encoding] -x xlsName", posix: true);
cli.with {
    h longOpt:   'help',                                                         'Show usage information'
    v longOpt:   'verbose',                                                      'Create debug output'
    d longOpt:   'dirName',    required: false, args: 1, argName: 'dirName',     "Folder containing the message properties files [${DEFAULT_DIRNAME}]"
    p longOpt:   'pattern',    required: false, args: 1, argName: 'pattern',     "Pattern of the message properties files [${DEFAULT_PATTERN}]"
    x longOpt:   'xlsName',    required: true,  args: 1, argName: 'xlsName',     'Name of the XLS file'
    e longOpt:   'encoding',   required: false, args: 1, argName: 'encoding',    "Encoding of the message properties files [${DEFAULT_ENCODING}]"
    u longOpt:   'unescapeUTF-8', required: false,                               "Unescape UTF-8"
    c longOpt:   'createXLS',     required: false,                               "Create XLS file"
    n longOpt:   'noBlank',       required: false,                               "Delete properties with blank values"
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

boolean fVerbose        = options.v;
String dirName          = options.d ?: DEFAULT_DIRNAME;
String pattern          = options.p ?: DEFAULT_PATTERN;
String xlsName          = options.x;
String encoding         = options.e ?: DEFAULT_ENCODING;
boolean fUnescape       = options.u;
boolean fCreateXLSFile  = options.c;
boolean fNoBlank        = options.n;

// properties -> xls
String fileNameLocaleGroupPattern  = ".*?((_\\w{2,3}_\\w{2,3})|(_\\w{2,3})|())\\.\\w*";

// xls -> properties
String localeFilterRegex           = ".*"

AntBuilder ant = new AntBuilder();

ant.taskdef(name: 'i18nBinder', classname: 'org.omnaest.i18nbinder.I18nBinder') {
  //classpath {
  //  pathelement(location: 'i18nbinder-0.4.0.BUILD-SNAPSHOT.sh');
  //}
}

if (fCreateXLSFile) {
  ant.i18nBinder(createXLSFile: fCreateXLSFile, xlsfilename: xlsName, fileEncoding: encoding, useJavaStyleUnicodeEscaping: fUnescape, fileNameLocaleGroupPattern: fileNameLocaleGroupPattern) {
    fileset(dir: dirName) {
      include(name: pattern);
    }
  }
} else {
  ant.i18nBinder(xlsfilename: xlsName, fileEncoding: encoding, useJavaStyleUnicodeEscaping: fUnescape, localeFilterRegex: localeFilterRegex, deletePropertiesWithBlankValue: fNoBlank)
}
