openapi:
	sbt "statelessOpenapi/run writeApiSpec ../api-spec/todo-api.yaml"

migration:
	sbt flywayMigrate

models:
	sbt scalikejdbcGenAllForce scalafmtAll

bin:
	sbt statelessAkkaHttp/packageXzTarball statelessOpenapi/packageXzTarball statedGraphqlOpenapi/packageXzTarball

test:
	sbt clean coverageOn test coverageAggregate

packageTxz:
	sbt packageAllTxz

image: packageTxz
	docker build --tag=fullplay-playground-scala-servers --file=./Dockerfile ./target/universal/
