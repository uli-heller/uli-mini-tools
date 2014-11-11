ASCIIDOC
========

Trying to package asciidoctorj in a single executable.

It looks like you cannot pack this into	a self-executable jar.
Doing so and executing it leads to this error message:

```
Exception in thread "main" org.jruby.exceptions.RaiseException: (LoadError) no \
such file to load -- asciidoctor
          at org.jruby.RubyKernel.require(org/jruby/RubyKernel.java:1083)
          at RUBY.(root)(<script>:9)
```
