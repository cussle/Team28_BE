#!/bin/bash

# 빌드 파일 탐색 및 이름 설정
BUILD_JAR=$(ls /home/ubuntu/app/build/libs/*-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo ">>> build 파일명: $JAR_NAME" >> /home/ubuntu/deploy.log

# 빌드 파일 복사
echo ">>> build 파일 복사" >> /home/ubuntu/deploy.log
DEPLOY_PATH=/home/ubuntu/app/
cp $BUILD_JAR $DEPLOY_PATH

# 현재 실행 중인 애플리케이션 종료
echo ">>> 현재 실행중인 애플리케이션 pid 확인 후 일괄 종료" >> /home/ubuntu/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]; then
    echo ">>> 현재 실행 중인 애플리케이션이 없습니다." >> /home/ubuntu/deploy.log
else
    echo ">>> 실행 중인 애플리케이션 종료: $CURRENT_PID" >> /home/ubuntu/deploy.log
    kill -15 $CURRENT_PID
    sleep 5
fi

# 애플리케이션 배포
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo ">>> DEPLOY_JAR 배포"    >> /home/ubuntu/deploy.log
echo ">>> $DEPLOY_JAR을 실행합니다" >> /home/ubuntu/deploy.log
# nohup java -jar $DEPLOY_JAR >> /home/ubuntu/deploy.log 2>&1 &
java -jar DevCard-0.0.1-SNAPSHOT.jar &  # 로그 확인용 임시
