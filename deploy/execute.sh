#!/usr/bin/env bash

# 임의로 설정
export BATCH_ENABLED=true

java \
  -Dspring.profiles.active=prod \
  -Duser.timezone=Asia/Seoul \
  -Dfile.encoding=UTF-8 \
  -Xms1G \
  -Xmx2G \
  -jar /home/ubuntu/spring-lotto.jar
