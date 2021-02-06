#!/usr/bin/env bash

set -uex
set -o pipefail

cd $(dirname "$0")

GENERATED_ROOT="$PWD/generated"
rm -rf $GENERATED_ROOT && mkdir -pv $GENERATED_ROOT

pushd ../server-openapi
make openapi > "$GENERATED_ROOT/todo-api.yaml"
popd


yarn openapi-generator-cli generate    \
    -g typescript-fetch                \
    -i "$GENERATED_ROOT/todo-api.yaml" \
    -c ./ts-fetch-options.json         \
    -o $GENERATED_ROOT/ts-fetch

yarn openapi-generator-cli generate    \
    -g typescript-rxjs                 \
    -i "$GENERATED_ROOT/todo-api.yaml" \
    -o $GENERATED_ROOT/ts-rx

yarn prettier --write "$GENERATED_ROOT"
