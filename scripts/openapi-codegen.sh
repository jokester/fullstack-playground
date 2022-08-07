#!/usr/bin/env bash

set -uex
set -o pipefail

cd $(dirname "$0")
SCRIPT_DIR=$(dirname "$0")
SPEC_DIR="$PWD/../api-spec"
CLIENT_ROOT="$PWD/../client-web"

generate-web () {
  local OPENAPI_YAML="$1"
  local GENERATED_ROOT="$2"
  rm -rf $GENERATED_ROOT && mkdir -pv $GENERATED_ROOT

  npm run openapi-generator-cli generate \
      -g typescript-fetch             \
      -i "$OPENAPI_YAML"    \
      -c ./ts-fetch-options.json      \
      -o "$GENERATED_ROOT"

  pushd "$CLIENT_ROOT"
  npm run prettier --write "$GENERATED_ROOT"
  popd
}

npm i
generate-web "$SPEC_DIR/todo-api.yaml" "$CLIENT_ROOT/src/todo-app-v1/generated"
generate-web "$SPEC_DIR/user-todo-api.yaml" "$CLIENT_ROOT/src/todo-app-v2/generated"
