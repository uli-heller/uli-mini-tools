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

## Issues

### Open

There are no open issues at the moment.

### Solved

#### java.lang.IllegalArgumentException: Buffering capacity exceeded

When using a parent proxy with parent proxy authentication, sometimes these error messages
show up within the log:

    2013-09-22 06:16:39,002 [ForwardProxyServlet-832293601-30] DEBUG org.eclipse.jetty.proxy.ProxyServlet$ProxyResponseListener:671-onHeaders() - 891110758 proxying to downstream:
    HttpContentResponse[HTTP/1.0 407 Proxy Authentication Required - 0 bytes]
    Server: squid/3.1.19M
    MIME-Version: 1.0M
    Date: Sun, 22 Sep 2013 04:15:31 GMTM
    Content-Type: text/htmlM
    Content-Length: 4160M
    X-Squid-Error: ERR_CACHE_ACCESS_DENIED 0M
    Vary: Accept-LanguageM
    Content-Language: de-deM
    Proxy-Authenticate: Basic realm="Squid proxy-caching web server"M
    X-Cache: MISS from localhostM
    X-Cache-Lookup: NONE from localhost:3128M
    Via: 1.0 localhost (squid/3.1.19)M
    Connection: keep-alive
    
    2013-09-22 06:16:39,003 [ForwardProxyServlet-832293601-30] DEBUG org.eclipse.jetty.proxy.ProxyServlet:490-onResponseContent() - 891110758 proxying content to downstream: 0 bytes
    2013-09-22 06:16:39,003 [ForwardProxyServlet-832293601-30] DEBUG org.eclipse.jetty.proxy.ProxyServlet:502-onResponseFailure() - 891110758 proxying failed
    java.lang.IllegalArgumentException: Buffering capacity exceeded
            at org.eclipse.jetty.client.util.BufferingResponseListener.onHeaders(BufferingResponseListener.java:67)
            at org.eclipse.jetty.client.ResponseNotifier.notifyHeaders(ResponseNotifier.java:107)
            at org.eclipse.jetty.client.ResponseNotifier.notifyHeaders(ResponseNotifier.java:99)
            at org.eclipse.jetty.client.HttpReceiver.headerComplete(HttpReceiver.java:252)
            at org.eclipse.jetty.http.HttpParser.parseHeaders(HttpParser.java:947)
            at org.eclipse.jetty.http.HttpParser.parseNext(HttpParser.java:1194)
            at org.eclipse.jetty.client.HttpReceiver.parse(HttpReceiver.java:117)
            at org.eclipse.jetty.client.HttpReceiver.receive(HttpReceiver.java:83)
            at org.eclipse.jetty.client.HttpConnection.receive(HttpConnection.java:273)
            at org.eclipse.jetty.client.HttpConnection.onFillable(HttpConnection.java:261)
            at org.eclipse.jetty.io.AbstractConnection$ReadCallback.run(AbstractConnection.java:358)
            at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:601)
            at org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:532)
            at java.lang.Thread.run(Thread.java:722)
    2013-09-22 06:16:39,004 [ForwardProxyServlet-832293601-30] DEBUG org.eclipse.jetty.proxy.ProxyServlet$ProxyResponseListener:725-onComplete() - 891110758 proxying complete


