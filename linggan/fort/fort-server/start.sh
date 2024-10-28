APPDIR=`pwd`
PIDFILE=$APPDIR/alert-executor.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "alert-executor is already running..."
exit 1
fi
nohup java -jar $APPDIR/alert-executor.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start alert-executor..."


