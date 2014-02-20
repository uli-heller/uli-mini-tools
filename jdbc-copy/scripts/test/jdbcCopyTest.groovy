import java.sql.SQLException;
import groovy.sql.Sql;

String from = '''
jdbc.driver=org.h2.Driver
jdbc.url=jdbc:h2:testFromH2
jdbc.username=scott
jdbc.password=tiger
''';

String to = '''
jdbc.driver=org.h2.Driver
jdbc.url=jdbc:h2:testToH2
jdbc.username=scott
jdbc.password=tiger
''';

File fromFile = File.createTempFile("from....", ".properties");
File toFile = File.createTempFile("to.....", ".properties");

Properties fromProperties = new Properties();
fromProperties.load(new StringReader(from));
Properties toProperties = new Properties();
toProperties.load(new StringReader(to));

def wipeout = { def sql ->
  def oldErr = System.err;
  System.setErr(new PrintStream(new OutputStream() {
    public void write(int b) {
    }
  }));
  try {
    sql.execute('''
      drop table DEALERS;
    ''');
  } catch (SQLException e) {
    //println e;
  }
  System.err = oldErr;
}

Sql fromSql = Sql.newInstance(fromProperties.'jdbc.url', fromProperties.'jdbc.username', fromProperties.'jdbc.password', fromProperties.'jdbc.driver');
Sql toSql = Sql.newInstance(toProperties.'jdbc.url', toProperties.'jdbc.username', toProperties.'jdbc.password', toProperties.'jdbc.driver');

println "Cleaning up";

wipeout(fromSql);
wipeout(toSql);

println "Creating tables";

fromSql.execute('''
  create table DEALERS (
    id integer not null primary key,
    name varchar(200),
    city varchar(200)
  )
''');


toSql.execute('''
  create table DEALERS (
    id integer not null primary key,
    name varchar(200),
    city varchar(200)
  )
''');

AntBuilder ant = new AntBuilder();

ant.java(classname: "groovy.ui.GroovyMain") {
  arg(value: "scripts/jdbcCopy.groovy")
}
