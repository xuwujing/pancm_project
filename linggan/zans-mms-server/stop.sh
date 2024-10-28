APPDIR=`pwd`
PIDFILE=$APPDIR/zans-mms-server.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "zans-mms-server not running..."
else
echo "stopping zans-mms-server..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...zans-mms-server stopped"
fi


