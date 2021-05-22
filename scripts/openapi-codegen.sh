#!/usr/bin/env bash

set -uex
set -o pipefail

cd $(dirname "$0")
SCRIPT_DIR=$(dirname "$0")
SPEC_DIR="$PWD/../api-spec"

save-spec () {
  pushd $SCRIPT_DIR/../server-openapi
  export WRITE_OPENAPI_SPEC_TO_FILE="$SPEC_DIR/todo-api.yaml"
  sbt "run writeOpenApiSpec"
  popd
}

generate-web () {
  local GENERATED_ROOT="$PWD/../client-web/src/generated"
  rm -rf $GENERATED_ROOT/openapi* && mkdir -pv $GENERATED_ROOT

  yarn openapi-generator-cli generate    \
      -g typescript-fetch                \
      -i "$SPEC_DIR/todo-api.yaml" \
      -c ./ts-fetch-options.json         \
      -o $GENERATED_ROOT/openapi-fetch

  yarn prettier --write "$GENERATED_ROOT"/openapi*
}

yarn
save-spec
generate-web
