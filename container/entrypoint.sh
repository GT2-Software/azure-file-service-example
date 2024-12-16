#!/bin/sh
set -e
echo "Entrypoint script is running"
/usr/sbin/sshd
#adduser sop_prod -D
#exec java -Djava.security.egd=file:///dev/urandom -XX:MaxRAMPercentage=${MEM_MAX_RAM_PERCENTAGE} -Xss${MEM_XSS} -XX:+UseContainerSupport ${JVM_ARGS} -Xmx12g -XX:ParallelGCThreads=4 -XX:ConcGCThreads=2 -Djava.util.concurrent.ForkJoinPool.common.parallelism=4 -jar payara-micro.jar "$@"
exec java -jar payara-micro.jar "$@"
