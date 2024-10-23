APPDIR=`pwd`
PIDFILE=$APPDIR/asyn-server.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "asyn-server not running..."
else
echo "stopping asyn-server..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...asyn-server stopped"
fi


