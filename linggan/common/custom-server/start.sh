APPDIR=`pwd`
PIDFILE=$APPDIR/custom-server.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "custom-server is already running..."
exit 1
fi
nohup java -jar $APPDIR/custom-server.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start custom-server..."


