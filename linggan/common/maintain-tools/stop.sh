APPDIR=`pwd`
PIDFILE=$APPDIR/maintain-tools.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "maintain-tools not running..."
else
echo "stopping maintain-tools..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...maintain-tools stopped"
fi


