
launch-postgres:
		docker run --name some-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres

flyway-migrate:
	flyway -url=jdbc:postgresql://localhost:5432/code \
       -user=postgres -password=postgres \
       -locations=filesystem:$(PWD)/code-service/src/main/resources/db.migration -outOfOrder=true migrate

flyway-repair:
	flyway -url=jdbc:postgresql://localhost:5432/code \
       -user=postgres -password=postgres \
       -locations=filesystem:$(PWD)/code-service/src/main/resources/db.migration -outOfOrder=true repair