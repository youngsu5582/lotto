#!/usr/bin/env bash

# 임의로 설정
export BATCH_ENABLED=true

APP_PATH=${APP_PATH:-"/home/ubuntu/spring-lotto.jar"}

XMS=${XMS:-"1G"}
XMX=${XMX:-"2G"}

if [ ! -f "$APP_PATH" ]; then
  echo "Error: JAR file not found at $APP_PATH"
  exit 1
fi

java \
  -Dspring.profiles.active=prod \
  -Duser.timezone=Asia/Seoul \
  -Dfile.encoding=UTF-8 \
  -Xms${XMS} \
  -Xmx${XMX} \
  -jar ${APP_PATH} || { echo "Application failed to start"; exit 1; }
