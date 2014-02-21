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
fromFile.setText(from);
File toFile = File.createTempFile("to.....", ".properties");
toFile.setText(to);

Properties fromProperties = new Properties();
fromProperties.load(new StringReader(from));
Properties toProperties = new Properties();
toProperties.load(new StringReader(to));

def createNewSql = { Properties p ->
  return Sql.newInstance(p.'jdbc.url', p.'jdbc.username', p.'jdbc.password', p.'jdbc.driver');
}

def count = { Sql sql, String tableName ->
  def result = sql.rows("select count(1) c from ${tableName};" as String);
  return result[0][0];
}

def createTableDealers = { Sql sql ->
  sql.execute('''
    create table DEALERS (
      id integer not null primary key,
      name varchar(200),
      city varchar(200)
    )
  ''');
}

def dropTableDealers = { Sql sql ->
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
  System.setErr(oldErr);
}

AntBuilder ant = new AntBuilder();

def result;

// --------------------------------------------------------------------------------------------
// Copy an empty table to an empty table
// --------------------------------------------------------------------------------------------

[fromProperties, toProperties].each {
  Sql sql = createNewSql(it);

  dropTableDealers(sql);
  createTableDealers(sql);
  sql.commit();
  sql.close();
}

ant.java(classname: "groovy.ui.GroovyMain", classpath: System.getProperty("java.class.path"), fork: true) {
  arg(value: "scripts/jdbcCopy.groovy")
  arg(value: "-f");
  arg(value: fromFile.getAbsolutePath());
  arg(value: "-t");
  arg(value: toFile.getAbsolutePath());
  arg(value: "--complete");
  arg(value: "dealers");
}

toSql = createNewSql(toProperties);

result = count(toSql, 'dealers');
assert result == 0;
toSql.close();

// --------------------------------------------------------------------------------------------
// Copy a non-empty table to an empty table
// --------------------------------------------------------------------------------------------

[fromProperties, toProperties].each {
  Sql sql = createNewSql(it);

  dropTableDealers(sql);
  createTableDealers(sql);
  sql.commit();
  sql.close();
}
[fromProperties].each {
  Sql sql = createNewSql(it);
  [
    [ id:10, name:'Edeka', city:'Kornwestheim' ],
    [ id:20, name:'Rewe',  city:'Ludwigsburg'  ],
    [ id:30, name:'Real',  city:'Stuttgart'    ],
  ].each {
    sql.execute("insert into dealers(id, name, city) values (${it.id}, ${it.city}, ${it.city})");
  } 
  sql.commit();
  sql.close();
}

ant.java(classname: "groovy.ui.GroovyMain", classpath: System.getProperty("java.class.path"), fork: true) {
  arg(value: "scripts/jdbcCopy.groovy")
  arg(value: "-f");
  arg(value: fromFile.getAbsolutePath());
  arg(value: "-t");
  arg(value: toFile.getAbsolutePath());
  arg(value: "--complete");
  arg(value: "dealers");
}

toSql = createNewSql(toProperties);

result = count(toSql, 'dealers');
assert result == 3;
toSql.close();
