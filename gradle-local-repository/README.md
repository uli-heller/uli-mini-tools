GRADLE-LOCAL-REPOSITORY
=======================

A small tool which allows you to create a local ivy repository from the gradle cache.
Typically, you'll populate the the local ivy repository like this:

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
