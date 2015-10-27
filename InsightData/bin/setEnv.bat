@echo off

set ANT_HOME=D:\Insight\apache-ant-1.9.6
set JAVA_HOME=D:\SPL\FW\4.2.0.5.0\Code\tools\jdk1.6.0_20

set PATH=%PATH%;%ANT_HOME%\bin;%JAVA_HOME%\bin;

set LIB_HOME=.\..\lib
set CLASSPATH=%LIB_HOME%\insight-data.jar;%LIB_HOME%\super-csv-2.3.1.jar;

@echo on
