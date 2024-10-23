APPDIR=`pwd`
PIDFILE=$APPDIR/alert-api.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "alert-api not running..."
else
echo "stopping alert-api..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...alert-api stopped"
fi


