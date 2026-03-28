@echo off
javac -sourcepath ./jbcs/src -d ./jbcs/bin ./jbcs/src/jbcs/JbcsMain.java
java -cp jbcs/bin jbcs.JbcsMain
pause
