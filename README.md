## Contenerizacion

```pwsh
docker build -f .\Dockerfile.local -t meza360/azure-storage-service:latest .
```

```pwsh
docker run -d -p 8080:8080 `
--mount type=volume,source=vazurefileservice,target=/mnt/volumes/vazurefileservice `
-e LOG_ROOT_PATH=/mnt/volumes/vazurefileservice/log `
-e DEPLOYMENT_ARCH=Container `
meza360/azure-file-service:latest
```

```bash
docker run -d -p 8080:8080 \
--mount type=volume,source=vazurefileservice,target=/mnt/volumes/vazurefileservice \
-e LOG_ROOT_PATH=/mnt/volumes/vazurefileservice/log \
meza360/azure-file-service:latest
```


```bash
# Restart a deployment
kubectl rollout restart deployment/file-service
```
## Generar .war
Esto genera un .war sin variables de entorno
```bash
mvn clean package
```

## Servidor Middleware
Se utiliza Payara Micro para contenerizacion del aplicativo en la arquitectura cloud. Se utiliza un archivo de entrypoint personalizado [entrypoint.sh](./container/entrypoint.sh) 


Se necesitan establecer las variables para el funcionamiento del aplicativo transparente desde onPremise:


Kubectl command failed: The Service "api-service" is invalid: spec.ports[0].nodePort: Invalid value: 8102: provided port is not in the valid range. The range of valid ports is 30000-32767
