#!/bin/bash

# Comando Docker para ejecutar un contenedor de MongoDB
docker run -d \
    --name mongo-server \
    -e MONGO_INITDB_ROOT_USERNAME="agencia_viajes" \
    -e MONGO_INITDB_ROOT_PASSWORD="password" \
    -p 28017:27017 \
    mongo:latest