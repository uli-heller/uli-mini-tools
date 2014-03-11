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
./scripts/groovy.sh ./scripts/test/jdbcCopyTest.groovy
```

Issues
------

### Adding A Custom JDBC Driver

TBD
