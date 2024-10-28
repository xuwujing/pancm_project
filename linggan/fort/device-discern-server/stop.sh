APPDIR=`pwd`
PIDFILE=$APPDIR/device-discern-server.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "device-discern-server not running..."
else
echo "stopping device-discern-server..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...device-discern-server stopped"
fi


