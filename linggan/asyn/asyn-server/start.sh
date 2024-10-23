APPDIR=`pwd`
PIDFILE=$APPDIR/asyn-server.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "asyn-server is already running..."
exit 1
fi
nohup java -jar $APPDIR/asyn-server.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start asyn-server..."


