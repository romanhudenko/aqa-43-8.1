#!/bin/sh
java -jar ./artifacts/app-deadline.jar -P:jdbc.url=jdbc:mysql://localhost:3306/cards -P:jdbc.user=petya -P:jdbc.password=qwerty123
rm -fR ./mysql_data
docker-compose restart