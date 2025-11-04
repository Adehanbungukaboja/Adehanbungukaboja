#!/bin/bash
cd "$(dirname "$0")"
javac -encoding UTF-8 AutoGitHelper.java
java -Dfile.encoding=UTF-8 AutoGitHelper
read -p "Press Enter to continue..."
