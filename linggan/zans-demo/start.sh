APPDIR=`pwd`
PIDFILE=$APPDIR/zans-demo.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "zans-demo is already running..."
exit 1
fi
nohup java -jar $APPDIR/zans-demo.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start zans-demo..."


