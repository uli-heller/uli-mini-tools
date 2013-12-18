ULI-MINI-TOOLS
==============

This project contains some of my small Java based tools.

* base64 ... prints the base64 encoded file content
* forward-proxy ... a forward proxy based on Jetty, see [forward-proxy/README.md](forward-proxy/README.md) for details
* gradle-local-repository ... copies the gradle cache into a local ivy repo, see [gradle-local-repository/README.md](gradle-local-repository/README.md) for details
* hexdump ... prints a hexdump of a file (similar to 'od')
* html-unescape ... unescape all html entities included in a file
* http-cat ... download a http url and print it to stdout
* little-proxy ... a forward proxy based on LittleProxy, see [little-proxy/README.md](little-proxy/README.md) for details
* md5sum ... calculate the md5 hash of a file; emulates the unix command "md5sum"
* sha1 ... calculate the SHA1 hash of a file; emulates the unix command "sha1sum"
* sha2 ... calculate the SHA256 hash of a file; emulates the unix command "sha256sum"
* tcpmon ... monitors tcp connections
* xmldiff ... shows the differences between xml files (based on xmlunit)

See <https://github.com/uli-heller/uli-mini-tools-downloads> for precompiled
binaries.

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

### 0.4.0 (not yet released)

* gradle-local-repository: Fixed error handling

* hexdump: New tool

* xmldiff: ignore attribute order, ignore comments, normalizeWhitespace, normalize

* gradle: Use version 1.10 (used to be 1.9)

### 0.3.0

No written history
