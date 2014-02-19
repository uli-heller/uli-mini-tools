#!/bin/sh

D="$(dirname "$0")"
LIBS="${D}/../build/output/libs"
exec java -cp "${LIBS}/*" groovy.ui.GroovyMain "$@"
