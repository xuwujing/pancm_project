APPDIR=`pwd`
PIDFILE=$APPDIR/leakproof-server.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "leakproof-server not running..."
else
echo "stopping leakproof-server..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...leakproof-server stopped"
fi


