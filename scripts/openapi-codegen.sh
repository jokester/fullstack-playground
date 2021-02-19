#!/usr/bin/env bash

set -uex
set -o pipefail

cd $(dirname "$0")
SPEC_DIR="$PWD/../api-spec"

generate-web () {
  local GENERATED_ROOT="$PWD/../client-web/src/generated"
  rm -rf $GENERATED_ROOT/openapi* && mkdir -pv $GENERATED_ROOT

  yarn openapi-generator-cli generate    \
      -g typescript-fetch                \
      -i "$SPEC_DIR/todo-api.yaml" \
      -c ./ts-fetch-options.json         \
      -o $GENERATED_ROOT/openapi-fetch

  yarn openapi-generator-cli generate    \
      -g typescript-rxjs                 \
      -i "$SPEC_DIR/todo-api.yaml" \
      -o $GENERATED_ROOT/openapi-rx

  yarn prettier --write "$GENERATED_ROOT"/openapi*
}

generate-web
