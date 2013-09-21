# Forward Proxy

This is a small java program implementing a forward proxy.
Most of the hard work is done by the jetty implementations.

* Jetty Proxy Server
* Jetty Proxy Servlet

For license information, see [here](LICENSE.md).

## Build Instructions

The build is based on [Gradle](http://gradle.org).
Currently, Forward Proxy is part of the master project "uli-mini-tools".
As such, the gradle installation of the master project can be used.
You can use other gradle installations as well. Just replace `../gradlew`
by the full path to your gradle executable.

### Full Build

For a full build, open a shell window and execute:

* `../gradlew`

within it. The output will typically look like this:

    :clean
    :compileJava
    :processResources
    :classes
    :jar
    :sh
    :bat
     
     BUILD SUCCESSFUL
     
     Total time: 7.266 secs

Afterwards, you'll have these build artifacts:

* forward-proxy.sh ... an executable working on Unix like systems
* forward-proxy.bat ... an executable for Windows like systems

### Incremental Build

An incremental build of the sh file can be done by executing:

* `../gradlew sh`

The output will typically look like this:

    :compileJava UP-TO-DATE
    :processResources UP-TO-DATE
    :classes UP-TO-DATE
    :jar UP-TO-DATE
    :sh
    
    BUILD SUCCESSFUL
    
    Total time: 6.007 secs

Afterwards, you'll have the build artifact:

* forward-proxy.sh ... an executable working on Unix like systems

If you'd like to create the forward-proxy.bat, just execute `../gradlew bat`.

### Cleanup

To cleanup the project folder, just execute:

* `../gradlew clean`

## Usage

If you want to use the forward proxy with its default configuration, you'll just
execute

* `./forward-proxy.sh`

after doing the build. This will start a proxy server listening to port 8888.
