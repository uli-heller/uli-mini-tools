GRADLE-LOCAL-REPOSITORY
=======================

A small tool which allows you to create a local ivy repository from the gradle cache.
This is helpful when working on a project in an environment where the internet access
is restricted. The workflow is this:

* Work on a machine which has online access to maven central an other artifact repos
* Execute various gradle build on this
* Create the local ivy repository by executing

      groovy ./src/main/groovy/copyArtifacts -g ~/.gradle -t ivy-repo

* Extend the repository list within your build.gradle:

      repositories {
        ivy {
          url 'ivy-repo'
        }
        mavenCentral()
      }

* Hopefully, you'll be able to work offline now!
* When you commit the ivy-repo into version control, your co-workers are able to
  work offline, too

Some Tests
----------

### build-xmlapis.gradle

    $ ../gradlew -b build-xmlapis.gradle -g xmlapis eclipse # Some downloads are required
    :eclipseClasspath
    Download http://repo1.maven.org/maven2/xml-apis/xml-apis/2.0.2/xml-apis-2.0.2.pom
    POM relocation to an other version number is not fully supported in Gradle : xml-apis#xml-apis;2.0.2 relocated to xml-apis#xml-apis;1.0.b2.
    Please update your dependency to directly use the correct version 'xml-apis#xml-apis;1.0.b2'.
    Resolution will only pick dependencies of the relocated element.  Artifacts and other metadata will be ignored.
    Download http://repo1.maven.org/maven2/xml-apis/xml-apis/1.0.b2/xml-apis-1.0.b2.pom
    Download http://repo1.maven.org/maven2/xml-apis/xml-apis/2.0.2/xml-apis-2.0.2-sources.jar
    :eclipseJdt
    :eclipseProject
    :eclipse
    
    BUILD SUCCESSFUL
    
    Total time: 6.943 secs
    
    $ ./gradle-local-repository-0.2.0.BUILD-SNAPSHOT.sh -g xmlapis -t ivy-repo
         [copy] Copying 1 file to /home/uli/git/uli-mini-tools/gradle-local-repository/ivy-repo/xml-apis/xml-apis/1.0.b2
         [copy] Copying 1 file to /home/uli/git/uli-mini-tools/gradle-local-repository/ivy-repo/xml-apis/xml-apis/2.0.2
         [copy] Copying 1 file to /home/uli/git/uli-mini-tools/gradle-local-repository/ivy-repo/xml-apis/xml-apis/2.0.2
    $ rm -rf xmlapis # Delete the gradle user home

    $ ../gradlew -b build-xmlapis.gradle -g xmlapis eclipse # No download required
    :eclipseClasspath
    :eclipseJdt
    :eclipseProject
    :eclipse
    
    BUILD SUCCESSFUL
    
    Total time: 6.483 secs

    $ rm -rf ivy-repo

### build-little-proxy.gradle

    $ ../gradlew -b build-little-proxy.gradle -g little-proxy eclipse # Lots of downloads
    :eclipseClasspath
    Download http://repo1.maven.org/maven2/org/littleshoot/littleproxy/1.0.0-beta3/littleproxy-1.0.0-beta3.pom
    Download http://repo1.maven.org/maven2/org/littleshoot/dnsjava/2.1.3/dnsjava-2.1.3-sources.jar
    Download http://repo1.maven.org/maven2/com/barchart/udt/barchart-udt-bundle/2.3.0/barchart-udt-bundle-2.3.0-sources.jar
    Download http://repo1.maven.org/maven2/com/google/guava/guava/14.0.1/guava-14.0.1-sources.jar
    Download http://repo1.maven.org/maven2/commons-io/commons-io/2.4/commons-io-2.4-sources.jar
    Download http://repo1.maven.org/maven2/io/netty/netty-all/4.0.7.Final/netty-all-4.0.7.Final-sources.jar
    Download http://repo1.maven.org/maven2/org/slf4j/slf4j-log4j12/1.7.2/slf4j-log4j12-1.7.2-sources.jar
    Download http://repo1.maven.org/maven2/org/littleshoot/littleproxy/1.0.0-beta3/littleproxy-1.0.0-beta3-sources.jar
    Download http://repo1.maven.org/maven2/org/apache/commons/commons-lang3/3.1/commons-lang3-3.1-sources.jar
    Download http://repo1.maven.org/maven2/commons-codec/commons-codec/1.7/commons-codec-1.7-sources.jar
    Download http://repo1.maven.org/maven2/log4j/log4j/1.2.17/log4j-1.2.17-sources.jar
    Download http://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.2/slf4j-api-1.7.2-sources.jar
    Download http://repo1.maven.org/maven2/commons-cli/commons-cli/1.2/commons-cli-1.2-sources.jar
    Download http://repo1.maven.org/maven2/org/littleshoot/dnssec4j/0.1/dnssec4j-0.1-sources.jar
    Download http://repo1.maven.org/maven2/org/littleshoot/littleproxy/1.0.0-beta3/littleproxy-1.0.0-beta3.jar
    :eclipseJdt
    :eclipseProject
    :eclipse
    
    BUILD SUCCESSFUL
    
    Total time: 13.738 secs
    
    $ ./gradle-local-repository-0.2.0.BUILD-SNAPSHOT.sh -g little-proxy -t ivy-repo
         [copy] Copying 1 file to /home/uli/git/uli-mini-tools/gradle-local-repository/ivy-repo/org.apache/apache/9
         [copy] Copying 1 file to /home/uli/git/uli-mini-tools/gradle-local-repository/ivy-repo/org.apache/apache/4
         [copy] Copying 1 file to /home/uli/git/uli-mini-tools/gradle-local-repository/ivy-repo/org.slf4j/slf4j-parent/1.7.2
    ...
         [copy] Copying 1 file to /home/uli/git/uli-mini-tools/gradle-local-repository/ivy-repo/org.littleshoot/dnssec4j/0.1
         [copy] Copying 1 file to /home/uli/git/uli-mini-tools/gradle-local-repository/ivy-repo/org.littleshoot/dnsjava/2.1.3
         [copy] Copying 1 file to /home/uli/git/uli-mini-tools/gradle-local-repository/ivy-repo/org.littleshoot/dnsjava/2.1.3
         [copy] Copying 1 file to /home/uli/git/uli-mini-tools/gradle-local-repository/ivy-repo/org.littleshoot/dnsjava/2.1.3
    
    $ rm -rf little-proxy/
    
    $ ../gradlew -b build-little-proxy.gradle -g little-proxy eclipse # No download
    :eclipseClasspath
    :eclipseJdt
    :eclipseProject
    :eclipse
    
    BUILD SUCCESSFUL
    
    Total time: 7.334 secs
    
    $ rm -rf ivy-repo
