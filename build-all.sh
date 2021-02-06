#!/usr/bin/env bash

set -uex

cd $(dirname "$0")

# openapi decl + client
pushd codegen-openapi
./run-generator.sh
popd
