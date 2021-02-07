#!/usr/bin/env bash

cd $(dirname "$0")
exec yarn graphql-codegen --config graphql-codegen.js "$@"

