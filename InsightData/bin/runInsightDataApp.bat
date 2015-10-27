@echo off

call setEnv.bat

@echo off

java -cp %CLASSPATH% com.insight.app.CoreEngineCLI %1 %2 %3 

@echo on
