APPDIR=`pwd`
PIDFILE=$APPDIR/device-discern-server.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "device-discern-server is already running..."
exit 1
fi
nohup java -jar $APPDIR/device-discern-server.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start device-discern-server..."


