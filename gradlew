#!/bin/sh
export JAVA_HOME=${JAVA_HOME:-$(dirname $(dirname $(readlink -f $(which java))))}
exec "$(dirname "$0")/gradle/wrapper/gradle-wrapper.jar" "$@"
