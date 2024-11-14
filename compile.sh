#!/bin/bash

echo "Creating directories..."
mkdir -p bin
mkdir -p data
mkdir -p src/repository/mapper

# Clean bin directory first
rm -rf bin/*

echo "Compiling Java files..."

# Set classpath to include bin directory
CLASSPATH="bin"

# Compilation steps
compilation_steps=(
    # 1. Compile constants, enums, and base
    "src/util/Constant.java"
    "src/enums/*.java"
    "src/model/Entity.java"
    "src/model/BaseEntity.java"
    "src/controller/Controller.java"
    "src/repository/mapper/BaseMapper.java"
    "src/util/*.java"
    "src/validator/*.java"
    
    # 2. Compile all models first
    "src/model/*.java"
    
    # 3. Compile mapper implementations
    "src/repository/mapper/*.java"
    
    # 4. Compile repository base
    "src/repository/base/CsvFileManager.java"
    "src/repository/base/CsvRepository.java"
    
    # 5. Compile repositories
    "src/repository/*.java"
    
    # 6. Rest of the application
    "src/service/*.java"
    "src/ui/*.java"
    "src/controller/BaseController.java"
    "src/controller/*.java"
    "src/main/*.java"
)

# Function to compile with error checking
compile_step() {
    echo "Compiling: $1"
    if ! javac -d bin -cp "$CLASSPATH" $1; then
        echo "Compilation failed at: $1"
        exit 1
    fi
}

# Execute compilation steps
for step in "${compilation_steps[@]}"; do
    # Check if files exist for the pattern
    if ls $step 1> /dev/null 2>&1; then
        compile_step "$step"
    else
        echo "No files found for pattern: $step (skipping)"
    fi
done

echo "Compilation complete."

# Check if Main class exists before running
if [ -f "bin/main/Main.class" ]; then
    echo "Running the application..."
    java -cp bin main.Main
else
    echo "Error: Main class not found!"
    exit 1
fi