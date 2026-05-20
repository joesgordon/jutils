@echo off
javac -sourcepath ./bukl/src -d ./bukl/bin ./bukl/src/bukl/BuklMain.java
java -cp bukl/bin bukl.BuklMain
pause
