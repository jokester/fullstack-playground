#!/usr/bin/env bash

set -uex
cd $(dirname "$0")
exec node_modules/.bin/gq http://127.0.0.1:61080/v1/graphql -H 'x-hasura-admin-secret: super_secret_admin_secret' --introspect > ../api-spec/schema.graphql

