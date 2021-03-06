ULI-MINI-TOOLS
==============

This project contains some of my small Java based tools.

* base64 ... prints the base64 encoded file content
* forward-proxy ... a forward proxy based on Jetty, see [forward-proxy/README.md](forward-proxy/README.md) for details
* gradle-local-repository ... copies the gradle cache into a local ivy repo, see [gradle-local-repository/README.md](gradle-local-repository/README.md) for details
* hexdump ... prints a hexdump of a file (similar to 'od')
* html-unescape ... unescape all html entities included in a file
* http-cat ... download a http url and print it to stdout
* i18nbinder ... translate between a set of properties files and an XLS file [i18nbinder/README.md](i18nbinder/README.md)
* jdbc-copy ... work in progress [jdbc-copy/README.md]/jdbc-copy/README.md)
* ln ... create links
* little-proxy ... a forward proxy based on LittleProxy, see [little-proxy/README.md](little-proxy/README.md) for details
* md5sum ... calculate the md5 hash of a file; emulates the unix command "md5sum"
* sha1 ... calculate the SHA1 hash of a file; emulates the unix command "sha1sum"
* sha256 ... calculate the SHA256 hash of a file; emulates the unix command "sha256sum"
* sha512 ... calculate the SHA512 hash of a file; emulates the unix command "sha512sum"
* tcpmon ... monitors tcp connections
* tika-app ... extract text from varios files
* wsdldiff ... shows the differences between wsdl files (based on soa-model-*)
* xmldiff ... shows the differences between xml files (based on xmlunit)

Precompiled binaries are available on the [releases](../../releases) page.

Build
-----

To build the project, execute

* on Linux: `./gradlew`
* on Windows: `.\gradlew.bat`

Obsolete Projects
-----------------

### asciidoctor

It looks like you cannot pack this into a self-executable jar.
Doing so and executing it leads to this error message:

```
Exception in thread "main" org.jruby.exceptions.RaiseException: (LoadError) no such file to load -- asciidoctor
	  at org.jruby.RubyKernel.require(org/jruby/RubyKernel.java:1083)
	  at RUBY.(root)(<script>:9)
```

License
-------

See [LICENSE.txt](LICENSE.txt).

Version History
---------------

### 0.7.2 (not yet released)

* no changes so far

### 0.7.1 2019-01-22

* http-cat: new option "-u username:password"
* wikitext: compile to java8 (required by wikitext-2.11)
* wikitext: upgraded mylyn wikitext from 2.3 -> 2.6 -> 2.11
* gradle: 2.4 -> 2.5 -> 2.6-rc-1 -> 2.6 -> 2.8 -> 2.10-rc-1 -> 2.10 -> 2.11

### 0.7.0 2017-06-27

* gradle-local-repository: New option -m - create mvn repo
* scp: started - doesn't work yet
* Use groovy-2.4.3 to compile all groovy scripts
* tika-app: New tool - basically just a very small wrapper around tika-app.jar
* gradle: 2.0 -> 2.1-rc-1 -> 2.1-rc-2 -> 2.1-rc-3 -> 2.1-rc-4 -> 2.1
   2.1 -> 2.2-rc-1 -> 2.2-rc-2 -> 2.2 -> 2.2.1 -> 2.4
* wikitext: upgraded mylyn wikitext from 2.1 -> 2.2 -> 2.3
* i18nbinder: Use 0.1.17u2

### 0.6.3 2014-07-06

* gradle-local-repository: Fixed handling of parent dependencies - copy poms

### 0.6.2 2014-07-06

* no improvements

### 0.6.1 2014-07-06

* no improvements

### 0.6.0 2014-07-05

* Use groovy-2.3.3 to compile all groovy scripts

* Renamed: sha1 -> sha1sum, sha256 -> sha256sum, sha512 -> sha512sum

* gradle-local-repository: Compatibility with gradle-2.0

### 0.5.1 2014-05-22

* jdbc-copy: New tool (work in progress)

* sha256: Renamed from sha2

* sha512: New tool

* wikitext: New tool

* wsdldiff: New tool

* Use groovy-2.3.0 to compile all groovy scripts

* gradlew: Use version 1.12 (used to be 1.10, 1.11)

* Fix: Run mini tools via PATH on Windows

### 0.5.0 - unpublished

### 0.4.0 - 2014-02-08

* gradle-local-repository: Fixed error handling

* hexdump: New tool

* i18nbinder: New tool

* ln: New tool

* xmldiff: ignore attribute order, ignore comments, normalizeWhitespace, normalize

* gradle: Use version 1.10 (used to be 1.9)

### 0.3.0

No written history
