APPDIR=`pwd`
PIDFILE=$APPDIR/maintain-tools.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "maintain-tools is already running..."
exit 1
fi
nohup java -jar $APPDIR/maintain-tools.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start maintain-tools..."


