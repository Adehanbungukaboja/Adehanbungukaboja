@echo off
chcp 65001 >nul
javac -encoding UTF-8 AutoGitHelper.java
java -Dfile.encoding=UTF-8 AutoGitHelper
pause
