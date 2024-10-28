APPDIR=`pwd`
PIDFILE=$APPDIR/device-cipher-server.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "device-cipher-server is already running..."
exit 1
fi
nohup java -jar $APPDIR/device-cipher-server.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start device-cipher-server..."


