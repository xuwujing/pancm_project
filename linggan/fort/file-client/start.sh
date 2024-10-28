APPDIR=`pwd`
PIDFILE=$APPDIR/file-client.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "file-client is already running..."
exit 1
fi
nohup java -jar $APPDIR/file-client.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start file-client..."


