@echo off
setlocal EnableDelayedExpansion
color 3e

PUSHD %~DP0 & cd /d "%~dp0"
%1 %2
mshta vbscript:createobject("shell.application").shellexecute("%~s0","goto :runas","","runas",1)(window.close)&goto :eof
:runas
start javaw -jar ftp-down.jar
exit
