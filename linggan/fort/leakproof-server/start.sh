APPDIR=`pwd`
PIDFILE=$APPDIR/leakproof-server.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "leakproof-server is already running..."
exit 1
fi
nohup java -jar $APPDIR/leakproof-server.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start leakproof-server..."


