#!/bin/sh
#. $HOME/.bashrc
set -e
echo "Entrypoint script is running"
chmod 0644 /etc/periodic/cron-log-files
mkdir -p ${LOG_ROOT_PATH}
touch ${LOG_ROOT_PATH}/cron.log
chmod 777 ${LOG_ROOT_PATH}/cron.log
crontab /etc/periodic/cron-log-files
crond -f /etc/periodic/cron-log-files &
#cron
#rc-service crond start && rc-update add crond

#/usr/sbin/sshd
#adduser sop_prod -D
#java -XshowSettings:properties -version
#exec java -Djava.security.egd=file:///dev/urandom -XX:MaxRAMPercentage=${MEM_MAX_RAM_PERCENTAGE} -Xss${MEM_XSS} -XX:+UseContainerSupport ${JVM_ARGS} -Xmx12g -XX:ParallelGCThreads=4 -XX:ConcGCThreads=2 -Djava.util.concurrent.ForkJoinPool.common.parallelism=4 -jar payara-micro.jar "$@"
exec java -Dkubernetes.log4j.useProperties="true" -jar payara-micro.jar "$@" "--deploymentDir" "/opt/payara/deployments" "--port" "8080"
#sleep infinity