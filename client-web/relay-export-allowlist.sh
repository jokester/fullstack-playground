#!/usr/bin/env bash

set -uex

cd $(dirname "$0")

API_SPEC="$PWD/../api-spec"

RELAY_EXPORT_QUERIES=YES yarn relay

# convert json to a hasura-allowlist format
ruby -rjson -e 'puts JSON.parse($<.read).values.sort.join("\n\n")' \
  < "$API_SPEC/relay-queries.json"                                 \
  > "$API_SPEC/relay-queries.graphql"
