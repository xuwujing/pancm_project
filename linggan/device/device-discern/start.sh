APPDIR=`pwd`
PIDFILE=$APPDIR/device-discern.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "device-discern is already running..."
exit 1
fi
nohup java -jar $APPDIR/device-discern.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start device-discern..."


