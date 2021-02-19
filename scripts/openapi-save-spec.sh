#!/usr/bin/env bash

set -uex
set -o pipefail

SCRIPT_DIR=$(dirname "$0")

pushd $SCRIPT_DIR/../server-openapi
make openapi > "../api-spec/todo-api.yaml"
