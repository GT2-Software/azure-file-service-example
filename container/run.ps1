docker run -d -p 8080:8080 `
--mount type=volume,source=vazurefileservice,target=/mnt/volumes/vazurefileservice `
-e LOG_ROOT_PATH=/mnt/volumes/vazurefileservice/log `
meza360.docker.io/azure-file-service:latest