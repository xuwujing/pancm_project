APPDIR=`pwd`
PIDFILE=$APPDIR/asyn-client.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "asyn-client is already running..."
exit 1
fi
nohup java -jar $APPDIR/asyn-client.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start asyn-client..."


