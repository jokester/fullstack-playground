#!/usr/bin/env bash

set -uex
set -o pipefail

SCRIPT_DIR=$(dirname "$0")

generate-web () {
  local GENERATED_ROOT="$SCRIPT_DIR/../client-web/src/generated"
  rm -rf $GENERATED_ROOT && mkdir -pv $GENERATED_ROOT
  pushd $SCRIPT_DIR/../server-openapi
  make openapi > "$GENERATED_ROOT/todo-api.yaml"
  popd

  yarn openapi-generator-cli generate    \
      -g typescript-fetch                \
      -i "$GENERATED_ROOT/todo-api.yaml" \
      -c ./ts-fetch-options.json         \
      -o $GENERATED_ROOT/openapi-fetch

  yarn openapi-generator-cli generate    \
      -g typescript-rxjs                 \
      -i "$GENERATED_ROOT/todo-api.yaml" \
      -o $GENERATED_ROOT/openapi-rx

  yarn prettier --write "$GENERATED_ROOT"
}

generate-web
