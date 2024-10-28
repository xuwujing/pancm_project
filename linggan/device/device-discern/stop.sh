APPDIR=`pwd`
PIDFILE=$APPDIR/device-discern.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "device-discern not running..."
else
echo "stopping device-discern..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...device-discern stopped"
fi


