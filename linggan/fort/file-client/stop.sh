APPDIR=`pwd`
PIDFILE=$APPDIR/file-client.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "file-client not running..."
else
echo "stopping file-client..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...file-client stopped"
fi


