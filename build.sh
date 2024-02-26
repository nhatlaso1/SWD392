#!/bin/sh

# Absolute path to this script, e.g. /home/user/bin/build.sh
SCRIPT=$(readlink -f "$0")
# Absolute path this script is in, thus /home/user/bin
SCRIPT_PATH=$(dirname "$SCRIPT")

cd "$SCRIPT_PATH";
gradle bootJar;

cd "$SCRIPT_PATH"/build/libs;
rm -rf application dependencies snapshot-dependencies spring-boot-loader
java -Djarmode=layertools -jar swd_392-*.jar extract
