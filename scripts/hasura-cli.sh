#!/usr/bin/env bash

<<END
### cheatsheet

$0 migrate apply                 # migrate
$0 migrate apply --down 1        # revert

###
END

set -ue
cd $(dirname "$0")/hasura
set -x

pwd

exec ../node_modules/.bin/hasura "$@"
