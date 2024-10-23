APPDIR=`pwd`
PIDFILE=$APPDIR/alert-executor.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "alert-executor not running..."
else
echo "stopping alert-executor..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...alert-executor stopped"
fi


