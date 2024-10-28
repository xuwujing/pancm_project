title create-link
@echo off

cmd.exe /c mklink /d C:\\Users\\Administrator\\Desktop\\%1 %2
:end
 
echo success;
