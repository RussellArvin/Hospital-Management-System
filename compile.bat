@echo off
echo Creating directories...
mkdir bin 2>nul
mkdir data 2>nul

echo Compiling Java files...
javac -d bin src/enums/*.java
javac -d bin -cp bin src/model/*.java
javac -d bin -cp bin src/repository/*.java
javac -d bin -cp bin src/service/*.java
javac -d bin -cp bin src/main/*.java

echo Compilation complete.
echo Running the application...
java -cp bin main.Main

pause