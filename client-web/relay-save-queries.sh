#!/usr/bin/env bash

set -uex

cd $(dirname "$0")

# appends to json
./relay-codegen.sh --persist-output "$PWD/../api-spec/relay-persisted-query.json"

# convert json to a hasura-allowlist format
ruby -rjson -e 'puts JSON.parse($<.read).values.join("\n\n")' \
  <"$PWD/../api-spec/relay-persisted-query.json"         \
  >"$PWD/../api-spec/relay-persisted-query.graphql"
