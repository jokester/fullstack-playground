server:
	sbt "run runServer"

openapi:
	sbt "run openapi"

migration:
	sbt flywayMigrate

models:
	sbt scalikejdbcGenAllForce scalafmtAll

bin:
	sbt universal:packageBin universal:packageXzTarball

image: bin
	docker build --tag=server-openapi-local --file=./Dockerfile target/universal/

.PHONY: target/universal/server-openapi-current.txz
