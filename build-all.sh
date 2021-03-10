#!/usr/bin/env bash

set -uex

cd $(dirname "$0")

# openapi decl + client
pushd server-openapi
  make openapi > ../api-spec/todo-api.yaml
popd

./scripts/openapi-codegen.sh
