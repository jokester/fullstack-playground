server:
	sbt "run runServer"

openapi:
	sbt "statelessOpenapi/run writeApiSpec ../api-spec/todo-api.yaml"

migration:
	sbt flywayMigrate

models:
	sbt scalikejdbcGenAllForce scalafmtAll

bin:
	sbt statelessAkkaHttp/packageXzTarball statelessOpenapi/packageXzTarball

image: bin
	docker build --tag=fullplay-playground-scala-servers --file=./Dockerfile ./target/universal/
