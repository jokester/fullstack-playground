#!/usr/bin/env bash

set -ue
cd $(dirname "$0")/hasura

# use assets in docker image instead of CDN
if ! [[ -d ../console-assets ]]; then
  docker cp fullstack-playground_hasura_1:/srv/console-assets ../hasura-console-assets
fi

set -x
exec ../node_modules/.bin/hasura console --static-dir=../hasura-console-assets
