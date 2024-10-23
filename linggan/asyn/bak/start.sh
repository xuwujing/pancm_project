APPDIR=`pwd`
PIDFILE=$APPDIR/asyn-consumer.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "asyn-consumer is already running..."
exit 1
fi
nohup java -jar $APPDIR/asyn-consumer.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start asyn-consumer..."


