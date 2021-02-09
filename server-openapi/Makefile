server:
	sbt "run runServer"

openapi:
	@#                           v to skip sbt output
	@sbt "run openapi" | grep -v -e '^\['

models:
	sbt scalikejdbcGenAllForce scalafmtAll

bin:
	sbt universal:packageBin universal:packageXzTarball

image: bin
	docker build --tag=server-openapi-local --file=./Dockerfile target/universal/

.PHONY: target/universal/server-openapi-current.txz
