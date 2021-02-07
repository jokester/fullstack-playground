#!/usr/bin/env bash

cd $(dirname "$0")

exec yarn gq http://127.0.0.1:61081/v1/graphql --introspect

