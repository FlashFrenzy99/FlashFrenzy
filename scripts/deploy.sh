#!/usr/bin/env bash

echo "> 최신 도커 이미지 받기"
sudo docker pull ${{ secrets.DOCKER_USERNAME }}/ff:latest

echo "> 실행중인 도커 컨테이너 정지"
sudo docker stop $(sudo docker ps -a -q)

echo "> 최신 이미지로 도커 컨테이너 실행"
sudo docker run -d --log-driver=syslog -p 8080:8080 ${{ secrets.DOCKER_USERNAME }}/ff:latest

echo "> 정지된 도커 컨테이너 삭제"
sudo docker rm $(sudo docker ps --filter 'status=exited' -a -q)

echo "> 사용하지 않는 도커 이미지 삭제 "
sudo docker image prune -a -f





