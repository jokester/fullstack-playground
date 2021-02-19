#!/usr/bin/env bash

set -uex

cd $(dirname "$0")

./relay-codegen.sh --persist-output "$PWD/../api-spec/relay-persisted-query.json"

ruby -rjson -e 'puts JSON.parse($<.read).values.join("\n\n")' \
  <"$PWD/../api-spec/relay-persisted-query.json"         \
  >"$PWD/../api-spec/relay-persisted-query.graphql"
