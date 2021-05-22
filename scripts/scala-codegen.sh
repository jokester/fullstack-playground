#!/usr/bin/env bash

set -uex
set -o pipefail

cd $(dirname "$0")/../openapi-server
exec sbt flywayMigrate scalikejdbcGenAllForce scalafmtAll
