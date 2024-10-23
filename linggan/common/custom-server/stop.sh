APPDIR=`pwd`
PIDFILE=$APPDIR/custom-server.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "custom-server not running..."
else
echo "stopping custom-server..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...custom-server stopped"
fi


