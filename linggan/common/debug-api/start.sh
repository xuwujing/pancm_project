APPDIR=`pwd`
PIDFILE=$APPDIR/debug-api.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "debug-api is already running..."
exit 1
fi
nohup java -jar $APPDIR/debug-api.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start debug-api..."


