@echo off & setlocal enabledelayedexpansion
echo "*********************************************************"
echo "*                    Hello hacker!!!                    *"
echo "*                    Created By Ray!                    *"
echo "*********************************************************"

:setEnv
set java_exe="java"
if not exist "%JAVA_HOME%\bin\java.exe" (
    if not exist "..\jdk\bin\java.exe" (
        echo "请配置Java运行环境"
        pause
        exit 1
    ) else (
        set java_exe="..\jdk\bin\java"
    )
)
goto start

:start
title Hacker chat room
del /Q ..\logs\*
if ""%1"" == ""debug"" goto debug

::%java_exe% -Xms512m -Xmx1024m %JAVA_OPTS% -classpath ..\conf;target\test-classes\UDPTest
java -classpath .\bin;.\bin\* Main
::java -jar ChatWithCmd-1.0-SNAPSHOT.jar
goto end

:debug
%java_exe% -Xms512m -Xmx1024m -Xdebug -Xnoagent %JAVA_OPTS% -Djava.compiler=NONE -Dfile.encoding=utf-8 -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n -classpath ..\conf;Main
goto end

:end
pause