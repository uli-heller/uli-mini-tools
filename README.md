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

TBD

License
-------

See [LICENSE.txt](LICENSE.txt).

Version History
---------------

### 0.7.0 (not yet released)

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
