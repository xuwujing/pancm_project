APPDIR=`pwd`
PIDFILE=$APPDIR/zans-mms-server.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "zans-mms-server is already running..."
exit 1
fi
nohup java -jar $APPDIR/zans-mms-server.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start zans-mms-server..."


