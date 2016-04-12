
set CURR_DIR=%~dp0
set LOGDOG_CLASSPATH=%CURR_DIR%..\lib\logdog.jar;%CURR_DIR%..\lib\jsch.jar;%CURR_DIR%..\lib\log4j.jar

start %JAVA_HOME%\bin\javaw.exe -cp %LOGDOG_CLASSPATH% com.aviayes.logdog.ui.MainWindow %CURR_DIR%
#%JAVA_HOME%\bin\java.exe -cp %LOGDOG_CLASSPATH% com.aviayes.logdog.ui.MainWindow %CURR_DIR%