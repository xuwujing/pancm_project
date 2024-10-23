APPDIR=`pwd`
PIDFILE=$APPDIR/upgrade-server.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "upgrade-server is already running..."
exit 1
fi
nohup java -jar $APPDIR/upgrade-server.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start upgrade-server..."


