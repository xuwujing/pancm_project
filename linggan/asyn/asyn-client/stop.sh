APPDIR=`pwd`
PIDFILE=$APPDIR/asyn-client.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "asyn-client not running..."
else
echo "stopping asyn-client..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...asyn-client stopped"
fi


