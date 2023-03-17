#!/bin/sh
set -e
cd "$(dirname "${0}")/.."
for SAMPLE_PATH in samples/*
do
  pushd "${SAMPLE_PATH}" > /dev/null
  ./gradlew -q build
  popd > /dev/null
done
