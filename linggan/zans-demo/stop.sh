APPDIR=`pwd`
PIDFILE=$APPDIR/zans-demo.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "zans-demo not running..."
else
echo "stopping zans-demo..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...zans-demo stopped"
fi


