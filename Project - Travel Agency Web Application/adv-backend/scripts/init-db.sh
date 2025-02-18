#!/bin/bash

# Nombre de la base de datos predeterminado
DEFAULT_DATABASE_NAME="agencia_viajes"

# Si se proporciona un argumento, se usa como nombre de la base de datos, si no, usa el nombre predeterminado.
DATABASE_NAME="${1:-$DEFAULT_DATABASE_NAME}"

# Ejecuta el comando para crear la base de datos en el servidor de MongoDB
docker exec mongo-server mongosh -u "agencia_viajes" -p "password" --authenticationDatabase admin --eval "db.getSiblingDB('$DATABASE_NAME')"