server:
	sbt "run runServer"

openapi:
	@#                           v to skip sbt output
	@sbt "run openapi" | grep -v -e '^\['

txz: bin # shorter to type
	sbt universal:packageXzTarball

bin:
	sbt universal:packageBin

image:
	docker build --file=./Dockerfile target/universal/

.PHONY: target/universal/server-openapi-current.txz
