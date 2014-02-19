jdbc-copy
=========

Development
-----------

### Quick Tests

```
../gradlew copyToOutputLibs
./scripts/groovy.sh scripts/jdbcCopy.groovy -f etc/h2-from.properties -t etc/h2-to.properties -c etc/tables.conf -a -v
```

### Thorough Tests

```
../gradlew dist
./jdbc-copy*sh -f etc/h2-from.properties -t etc/h2-to.properties -c etc/tables.conf -a -v
```

Issues
------

### Adding A Custom JDBC Driver

TBD
