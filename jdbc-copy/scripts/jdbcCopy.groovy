import java.sql.SQLException;
import groovy.sql.Sql;
import groovy.transform.ToString;

def cli = new CliBuilder(usage: "jdbc-copy [-v][-h] -f from -t to [-C | -i] [-c cfgFile] [-a | tablename1 tablename2 ...]", posix: true);
cli.with {
    h   longOpt:   'help',                                                     'Show usage information'
    v   longOpt:   'verbose',                                                  'Create debug output'
    a   longOpt:   'all',                                                      'Copy all tables mentioned in cfgFile'
    'C' longOpt:   'complete',                                                 'Copy all data'
    d   longOpt:   'dry-run',                                                  'Show SQL only, do not modify destination'
    'D' longOpt:   'delete',                                                   'Delete records on destination'
    c   longOpt:   'config',    required: false, args: 1, argName: 'cfgFile',  'Config containing table definitions'
    f   longOpt:   'from',      required: true,  args: 1, argName: 'fromCfg',  'Config containing source jdbc parameters'
    t   longOpt:   'to',        required: true,  args: 1, argName: 'toCfg',    'Config containing destination jdbc parameters'
};

def options = cli.parse(args);
if (!options) {
    System.err.println "Unable to parse command line options -> EXIT";
    return(1);
}

if (options.h) {
    cli.usage();
    return(0);
}

boolean fVerbose          = options.v;
boolean fAll              = options.a;
boolean fComplete         = options.C;
boolean fDelete           = options.D;
boolean fDryRun           = options.d;
String fromPropertiesName = options.f;
Properties fromProperties = new Properties();
fromProperties.load(new FileInputStream(fromPropertiesName));
String toPropertiesName   = options.t;
Properties toProperties = new Properties();
toProperties.load(new FileInputStream(toPropertiesName));

def tableNames = options.arguments();
def tables = [:];

if (options.c) {
    String tableConfiguration = options.c;
    File f = new File(tableConfiguration);
    if (! f.exists()) {
        System.err.println "Unable to read file '${tableConfiguration}' -> EXIT";
        System.exit(1);
    }
    ConfigObject configObject = new ConfigSlurper().parse(f.toURI().toURL());
    tables = configObject.tables;
    //println configObject.tables.inspect();
    if (fAll) {
        tableNames = tables.keySet();
    }
} else if (fAll) {
    System.err.println "You have to specify a cfgFile when using option '-a' -> EXIT";
    System.exit(1);
}

if (!tableNames) {
    System.err.println "No tables to copy -> EXIT";
    System.exit(1);
}

def log = {
    if (fVerbose) {
        println it;
    }
}

Sql fromSql = Sql.newInstance(fromProperties.'jdbc.url', fromProperties.'jdbc.username', fromProperties.'jdbc.password', fromProperties.'jdbc.driver');

Sql toSql = Sql.newInstance(toProperties.'jdbc.url', toProperties.'jdbc.username', toProperties.'jdbc.password', toProperties.'jdbc.driver');


tableNames.each { String tableName ->
    log "Processing table ${tableName}"
    def constructorArgs = [tableName: tableName, log: log, dryRun: fDryRun];
    def tablesEntry = tables.get(tableName);
    if (tablesEntry) {
        constructorArgs += tablesEntry;
    }
    def tableDescription = new tableDescription(constructorArgs);
    tableDescription.copy(fromSql, toSql, fComplete, fDelete);
}
System.exit(0);

@ToString(includeNames=true, excludes="log", ignoreNulls=true)
class tableDescription {
    String tableName;
    String id;
    String sequence;
    boolean dryRun;
    def log;

    public boolean hasId() {
        return this.id != null;
    }

    public def getMaxId(Sql sql) {
        def maxId = null;
        if (hasId()) {
            def result = sql.firstRow((String) "select max(${id}) m from ${tableName}");
            if (result != null && result.m != null) {
                maxId = result.m;
            }
        }
        return maxId;
    }

    private def max(def a, def b) {
        return (a > b) ? a : b;
    }

    public boolean hasSequence() {
        return this.sequence != null;
    }

    void setSequenceForTable(Sql sql, def lastUsedValue) {
        if (hasSequence()) {
            try {
                String drop = "drop sequence ${sequenceName}";
                log ".. ${tableName} - dropSequence: ${drop}";
                sqlExecute(sql, drop);
            } catch (SQLException e) {
                log ".. ${tableName} - unable to update sequence ${sequenceName} - ${e}";
            }
            try {
                String create = "create sequence ${sequenceName} start with ${lastUsedValue+1}";
                log ".. ${tableName} - createSequence: ${create}";
                sqlExecute(sql, create);
            } catch (SQLException e) {
                log ".. ${tableName} - unable to update sequence ${sequenceName} - ${e}";
            }
        }
    }

    long countId(Sql sql, def thisId) {
        String query = "select count(1) c from ${tableName} where ${id} = ${thisId}";
        def rows = sql.rows(query);
        return rows[0].c;
    }

    void delete(Sql sql, def minId, def maxId) {
        String delete = "delete from ${tableName}";
        List<String> criteria = [];
        if (minId) {
            criteria << "${id} > ${minId}";
        }
        if (maxId) {
            criteria << "${id} < ${maxId}";
        }
        if (criteria) {
            delete += " where ";
            delete += criteria.join(" and ");
        }
        log ".. ${delete}";
        sqlExecute(sql, delete);
    }

    void sqlExecute(Sql sql, String cmd) {
        if (dryRun) {
            println "SQL: ${cmd}";
        } else {
            sql.execute(cmd);
        }
    }

    void sqlExecute(Sql sql, String cmd, def row) {
        if (dryRun) {
            println "SQL: ${cmd} ${row}";
        } else {
            sql.execute(cmd, row);
        }
    }

    public void copy(Sql from, Sql to, boolean fAllRecords, boolean fDeleteRecords) {
        log "Starting to copy ${tableName}";
        log ".. ${tableName} - copying all records";
        def knownMaxId  = -1;  // highest known id
        long cnt = 0;   // count the number of records found in this table
        long insertCnt = 0;
        long updateCnt = 0;
        boolean thisTableHasId = hasId();
        String query = "select * from ${tableName}";
        if (! fAllRecords) {
            def maxId = getMaxId(from);
            query = "${query} where ${id} > ${maxId}"; // FIXME: Parameterize!
        }
        query += " order by ${id}";
        log ".. ${query}";
        def rows = from.rows(query);
        def previousId = null;
        for (def row : rows) {
            boolean fDoInsert = false;
            boolean fDoUpdate = false;
            def thisId;
            if (thisTableHasId) {
                thisId = row.get(id);
                knownMaxId = max(knownMaxId, thisId);
                long thisIdCnt = countId(to, thisId);
                if (thisIdCnt <= 0) {
                    fDoInsert = true;
                } else if (thisIdCnt > 1) {
                    log("ERROR - id ${thisId} is not unique within table ${tableName}");
                } else {
                    fDoUpdate = true;
                }
                if (fDeleteRecords) {
                    delete(to, previousId, thisId);
                }
                previousId = thisId;
            } else {
                // ! thisTableHasId
                fDoInsert = true;
            }
            def keySet = row.keySet();
            if (fDoInsert || fDoUpdate) {
                String sqlCommand;
                if (fDoInsert) {
                    ++insertCnt;
                    String insertFieldList = keySet.join(',');
                    def colonizedKeySet = keySet.collect{ ":${it}" };
                    String insertColonizedFieldList = colonizedKeySet.join(',')
                    sqlCommand = "insert into ${tableName} ( ${insertFieldList} ) values ( ${insertColonizedFieldList} )";
                } else if (fDoUpdate) {
                    assert thisId != null;
                    ++updateCnt;
                    def updateSet = keySet.collect{ "${it} = :${it}" };
                    sqlCommand = "update ${tableName} set ${updateSet.join(",")} where ${id}=${thisId}";
                }
                sqlExecute(to, sqlCommand, row);
            }
            to.commit(); // commit in order to free the blob objects
            ++cnt;
        }
        if (fDeleteRecords) {
            delete(to, previousId, null);
        }
        log ".. ${tableName} - numberOfRecords: ${cnt}, max id: ${knownMaxId}, insertCnt: ${insertCnt}, updateCnt: ${updateCnt}";
        if (cnt > 0) {
            setSequenceForTable(to, knownMaxId);
        }
    }
}
