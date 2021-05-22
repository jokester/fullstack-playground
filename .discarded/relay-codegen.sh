#!/usr/bin/env bash

set -ue

cd $(dirname "$0")

exec node_modules/.bin/relay-compiler "$@"
