server:
	sbt "run runServer"

txz: # shorter to type
	sbt packageXzTarball

target/universal/tapir-todoapi-current.txz: txz

image: Dockerfile target/universal/tapir-todoapi-current.txz
	docker build --file=./Dockerfile target/universal/

.PHONY: target/universal/tapir-todoapi-current.txz
