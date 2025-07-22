#!/bin/bash

DEPLOY_LOG_PATH=/home/ec2-user/app/deploy.log
DEPLOY_PATH=/home/ec2-user/app/
BUILD_JAR=$(ls /home/ec2-user/app/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)

export DATABASE_URL=$(aws ssm get-parameter --name /cloudtype-manager-params/database/url --query Parameter.Value | sed 's/"//g')
export DATABASE_USERNAME=$(aws ssm get-parameter --name /cloudtype-manager-params/database/username --query Parameter.Value | sed 's/"//g')
export DATABASE_PASSWORD=$(aws ssm get-parameter --name /cloudtype-manager-params/database/password --query Parameter.Value | sed 's/"//g')
export API_SERVER_URL=$(aws ssm get-parameter --name /cloudtype-manager-params/api-server-url --query Parameter.Value | sed 's/"//g')

echo ">>> Setting env, DATABASE_URL : $DATABASE_URL" >> $DEPLOY_LOG_PATH

echo ">>> build : $JAR_NAME" >> $DEPLOY_LOG_PATH

echo ">>> copy jar" >> $DEPLOY_LOG_PATH
cp $BUILD_JAR $DEPLOY_PATH

echo " 현재 실행중인 app pid 확인" >> $DEPLOY_LOG_PATH
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo ">>> 현재 구동중인 app이 없으므로 종료하지 않습니다." >> $DEPLOY_LOG_PATH
else
  echo ">>> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 10
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
nohup java -jar $DEPLOY_JAR --spring.profiles.active=prod >> $DEPLOY_LOG_PATH 2> /home/ec2-user/app/deploy_err.log &