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

# 에러는 별도 로그로 남긴다. 나머지, 입출력은 무시

nohup java \
  -Dspring.profiles.active=prod \
  -Duser.timezone=Asia/Seoul \
  -Dfile.encoding=UTF-8 \
  -Xms${XMS} \
  -Xmx${XMX} \
  -jar "${APP_PATH}" \
  > /dev/null 2> error.log < /dev/null &
