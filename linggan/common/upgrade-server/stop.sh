APPDIR=`pwd`
PIDFILE=$APPDIR/upgrade-server.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "upgrade-server not running..."
else
echo "stopping upgrade-server..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...upgrade-server stopped"
fi


