server:
	sbt "run runServer"

openapi:
	sbt "run openapi"

migration:
	sbt flywayMigrate

models:
	sbt scalikejdbcGenAllForce scalafmtAll

bin:
	sbt statelessAkkaHttp/packageXzTarball

image: bin
	docker build --tag=fullplay-playground-scala-servers --file=./Dockerfile ./target/universal/
