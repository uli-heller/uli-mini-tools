import java.sql.SQLException;
import groovy.sql.Sql;


def cli = new CliBuilder(usage: "createV1.groovy [-v][-h] -j jdbcParameters", posix: true);
cli.with {
    h   longOpt:   'help',                                                     'Show usage information'
    v   longOpt:   'verbose',                                                  'Create debug output'
    j   longOpt:   'jdbc',      required: true,  args: 1, argName: 'jdbcCfg',  'Config containing jdbc parameters'
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

boolean fVerbose          = options.v;
String jdbcPropertiesName = options.j;
Properties jdbcProperties = new Properties();
jdbcProperties.load(new FileInputStream(jdbcPropertiesName));

def log = {
  if (fVerbose) {
    println it;
  }
}

Sql sql = Sql.newInstance(jdbcProperties.'jdbc.url', jdbcProperties.'jdbc.username', jdbcProperties.'jdbc.password', jdbcProperties.'jdbc.driver');

log "Create table DEALERS"

sql.execute('''
  create table DEALERS (
    id integer not null primary key,
    name varchar(200),
    city varchar(200)
  )
''');

log "Create table OPTIONS"

sql.execute('''
  create table OPTIONS (
    option_id integer not null primary key,
    name varchar(200),
    description varchar(200)
  )
''');

sql.commit();
sql.close();
