APPDIR=`pwd`
PIDFILE=$APPDIR/device-cipher-server.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "device-cipher-server not running..."
else
echo "stopping device-cipher-server..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...device-cipher-server stopped"
fi


