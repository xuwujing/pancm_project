APPDIR=`pwd`
PIDFILE=$APPDIR/debug-api.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "debug-api not running..."
else
echo "stopping debug-api..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...debug-api stopped"
fi


