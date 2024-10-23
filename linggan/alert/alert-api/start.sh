APPDIR=`pwd`
PIDFILE=$APPDIR/alert-api.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "alert-api is already running..."
exit 1
fi
nohup java -jar $APPDIR/alert-api.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start alert-api..."


