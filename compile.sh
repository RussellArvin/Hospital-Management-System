#!/bin/bash
echo "Creating directories..."
mkdir -p bin
mkdir -p data

echo "Compiling Java files..."
javac -d bin src/enums/*.java
javac -d bin -cp bin src/model/*.java
javac -d bin -cp bin src/repository/base/*.java
javac -d bin -cp bin src/repository/mapper/*.java
javac -d bin -cp bin src/repository/*.java
javac -d bin -cp bin src/service/*.java
javac -d bin -cp bin src/main/*.java

echo "Compilation complete."
echo "Running the application..."
java -cp bin main.Main