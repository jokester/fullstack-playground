#!/usr/bin/env bash

set -uex
set -o pipefail

cd $(dirname "$0")
SCRIPT_DIR=$(dirname "$0")
SPEC_DIR="$PWD/../api-spec"
CLIENT_ROOT="$PWD/../client-web"

generate-web () {
  local API_ID="$1"
  local GENERATED_ROOT="$PWD/../client-web/src/generated/$API_ID"
  rm -rf $GENERATED_ROOT && mkdir -pv $GENERATED_ROOT

  yarn openapi-generator-cli generate \
      -g typescript-fetch             \
      -i "$SPEC_DIR/$API_ID.yaml"    \
      -c ./ts-fetch-options.json      \
      -o "$GENERATED_ROOT"

  pushd "$CLIENT_ROOT"
  yarn prettier --write "$GENERATED_ROOT"
  popd
}

yarn
generate-web "todo-api"
