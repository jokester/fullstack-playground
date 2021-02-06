server:
	sbt "run runServer"

openapi:
	#                           v to skip sbt output
	sbt "run openapi" | grep -v -e '^\[' > ../codegen-openapi/todo-api.yaml

txz: bin # shorter to type
	sbt universal:packageXzTarball

bin:
	sbt universal:packageBin

image:
	docker build --file=./Dockerfile target/universal/

.PHONY: target/universal/server-openapi-current.txz
