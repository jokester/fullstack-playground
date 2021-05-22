#!/usr/bin/env bash

<<END
### cheatsheet

$0 migrate apply                 # migrate
$0 migrate apply --down 1        # revert

###
END

set -ue
cd $(dirname "$0")/hasura

export HASURA_GRAPHQL_ENDPOINT='http://127.0.0.1:61081'
export HASURA_GRAPHQL_ADMIN_SECRET='super_secret_admin_secret'

set -x

pwd

if [[ "${1:-NULL}" = migrate ]]; then
  exec ../node_modules/.bin/hasura --database-name default "$@"
else
  exec ../node_modules/.bin/hasura "$@"
fi
