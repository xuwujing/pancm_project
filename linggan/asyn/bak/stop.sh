APPDIR=`pwd`
PIDFILE=$APPDIR/asyn-consumer.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "asyn-consumer not running..."
else
echo "stopping asyn-consumer..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...asyn-consumer stopped"
fi


