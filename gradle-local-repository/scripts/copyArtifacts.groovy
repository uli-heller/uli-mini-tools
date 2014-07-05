import groovy.transform.EqualsAndHashCode;
import groovy.transform.ToString;


def cli = new CliBuilder(usage: "copyArtifacts [-v][-h] -g gradleHome -t to", posix: true);
cli.with {
    h longOpt:   'help',                                                    'Show usage information'
    v longOpt:   'verbose',                                                 'Create debug output'
    g longOpt:   'gradle-home',  required: true, args: 1, argName: 'gradleHomeDir',  '...'
    t longOpt:   'to',           required: true, args: 1, argName: 'toDir',    '...'
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
String groovyHomeDirString = options.g;
String toDirString         = options.t;

File groovyHomeDir = new File(groovyHomeDirString);
File toDir         = new File(toDirString);

def allJars = [];
def allIvys = [];
def allPoms = [];

// search for jar
String matchJar = ".jar";
groovyHomeDir.eachFileRecurse { File file ->
  String name = file.getName();
  if (name.endsWith(matchJar)) {
    Jar jar = parseJar(file);
    if (jar != null) {
        allJars.add(jar);
    }
  }
}

// search for ivy.xml
String matchIvyXml = "ivy.xml";
groovyHomeDir.eachFileRecurse { File file ->
  if (matchIvyXml.equals(file.getName())) {
    allIvys.add(parseIvy(file));
  }
}

// search for pom files - not used at the moment
String matchPom = ".pom";
groovyHomeDir.eachFileRecurse { File file ->
  String name = file.getName();
  if (name.endsWith(matchPom)) {
    allPoms.add(parsePom(file));
  }
}

AntBuilder ant = new AntBuilder();
for (Ivy ivy : allIvys) {
  GroupArtifactVersion gav = ivy.groupArtifactVersion;
  File folder = new File(toDir, "${gav.group}/${gav.artifact}/${gav.version}"); 
  folder.mkdirs();
  // gradle-2.0 doesn't provide publications within the ivy.xml file,
  // so we'll create them...
  def xmlRoot = new XmlParser().parse(ivy.ivyXml);
  def publications = xmlRoot.'**'.publications[0];
  boolean fAddPublications = publications.children().size() <= 0;
  allJars.findAll{ jar -> jar.groupArtifactVersion.equals(gav) }.each {
    ant.copy(file: it.jar, todir: folder);
    if (fAddPublications) {
      publications.appendNode('artifact', [name: it.groupArtifactVersion.artifact, type: 'jar', ext: 'jar', conf: 'master']);
    }
  }
  def writer = new FileWriter(new File(folder, "ivy-${gav.version}.xml"));
  new XmlNodePrinter(new PrintWriter(writer)).print(xmlRoot);
  //ant.copy(file: ivy.ivyXml, tofile: new File(folder, "ivy-${gav.version}.xml"));
}

GroupArtifactVersion parseGroupArtifactVersion(File f) {
  File d1 = f?.getParentFile(); //...descriptors/org.littleshoot/dnsjava/2.1.3/e9d03b7c6586155fbee8fb2de8b5b149
  File d2 = d1?.getParentFile();     //...descriptors/org.littleshoot/dnsjava/2.1.3
  File d3 = d2?.getParentFile();     //...descriptors/org.littleshoot/dnsjava
  File d4 = d3?.getParentFile();     //...descriptors/org.littleshoot
  GroupArtifactVersion gav = null;
  if (d4 != null) {
    gav = new GroupArtifactVersion(group: d4.getName(), artifact: d3.getName(), version: d2.getName());
  }
  return gav;
}

@EqualsAndHashCode
@ToString(includeNames=true)
class GroupArtifactVersion {
  String group;
  String artifact;
  String version;
}

Ivy parseIvy (File ivyXml) {
  GroupArtifactVersion gav = parseGroupArtifactVersion(ivyXml);
  return new Ivy(groupArtifactVersion: gav, ivyXml: ivyXml);
}

@ToString(includeNames=true,excludes="ivyXml")
class Ivy {
  File ivyXml;
  GroupArtifactVersion groupArtifactVersion;
}

Jar parseJar (File jar) {
  GroupArtifactVersion gav = parseGroupArtifactVersion(jar);
  Jar parsedJar = null;
  if (gav != null) {
    parsedJar = new Jar(groupArtifactVersion: gav, jar: jar);
  }
  return parsedJar;
}

@ToString(includeNames=true,excludes="jar")
class Jar {
  File jar;
  GroupArtifactVersion groupArtifactVersion;
}

Pom parsePom (File pom) {
  GroupArtifactVersion gav = parseGroupArtifactVersion(pom);
  return new Pom(groupArtifactVersion: gav, pom: pom);
}

@ToString(includeNames=true,excludes="pom")
class Pom {
  File pom;
  GroupArtifactVersion groupArtifactVersion;
}
