#!/usr/bin/env bash
if [ $1 == "start" ]
then
  echo "start"
  nohup java -jar zans-mms-server.jar >> ./data.log 2>& 1 &
elif [ $1 == "stop" ]
then
  echo "stop "
  pids=$(ps aux | grep 'zans-mms-server.jar' | grep -v grep | awk '{print $2}')
  for pid in $pids
  do
    kill -9 $pid
  done
else
  echo "default"
fi
