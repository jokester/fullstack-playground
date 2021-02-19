#!/usr/bin/env bash

set -uex
cd $(dirname "$0")
exec node_modules/.bin/gq http://127.0.0.1:61081/v1/graphql --introspect > ../api-spec/schema.graphql

