## !/bin/bash

# Array de carpetas que podrían contener docker-compose.yml
folders=("eureka" "gateway" "authentication_service" "flights_service" "hotel_service" "mail_service")

# Ejecutar docker-compose en cada carpeta si el archivo docker-compose.yml existe
for folder in "${folders[@]}"; do
  if [ -f "$folder/docker-compose.yml" ]; then
    echo "Ejecutando docker-compose en la carpeta $folder"
    (cd "$folder" && docker-compose up -d)
  else
    echo "No se encontró docker-compose.yml en la carpeta $folder, omitiendo..."
  fi
done

# Ruta del script start.sh
start_script="scripts/start.sh"
sh scripts/init-db.sh flights_database

# Iniciar cada microservicio utilizando start.sh
for folder in "${folders[@]}"; do
  if [ -d "$folder" ]; then
    echo "Iniciando el microservicio en la carpeta $folder"
    start sh -c "sh $start_script $folder"
  else
    echo "No se encontró la carpeta $folder, omitiendo..."
  fi
done


echo "Todos los microservicios han sido iniciados."


root_folder="/"
activemq_folder="C:/apache-activemq-6.1.2-bin/apache-activemq-6.1.2/bin"
if [ -d "$root_folder" ]; then
  cd "$root_folder"
  if [ -d "$activemq_folder" ]; then
    echo "Moviéndose a la carpeta $activemq_folder y ejecutando activemq start"
    (cd "$activemq_folder" && ./activemq start)
  else
    echo "No se encontró la carpeta $activemq_folder, omitiendo..."
  fi
else
  echo "No se pudo mover a la carpeta raíz, omitiendo..."
fi

