## Contenerizacion

```bash
docker build -f .\Dockerfile.local -t meza360.docker.io/azure-file-service:latest .
```

```bash
docker run --rm --mount type=volume,target=/mnt/volumes/vazurefileservice,source=vazurefileservice -d -t -p 8080:8080 meza360.docker.io/azure-file-service:latest
```

## Generar .war
Esto genera un .war sin variables de entorno
```bash
mvn clean package
```

## Servidor Middleware
Se utiliza Payara Micro para contenerizacion del aplicativo en la arquitectura cloud. Debido a la concurrencia de conexiones de los paises, se necesita establecer un mecanismo de generacion de Ids de conexiones unicos de manera segura, para lo cual, se utiliza un archivo de entrypoint personalizado [entrypoint.sh](./container/entrypoint.sh) 


Se necesitan establecer las variables para el funcionamiento del aplicativo transparente, luego de la migracion desde onPremise: