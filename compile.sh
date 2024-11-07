#!/bin/bash
echo "Creating directories..."
mkdir -p bin
mkdir -p data

echo "Compiling Java files..."

# 1. First compile enums (since they are referenced by other classes)
javac -d bin src/enums/*.java

# 2. Compile base models and interfaces
javac -d bin -cp bin src/model/BaseEntity.java
javac -d bin -cp bin src/controller/Controller.java

# 3. Compile models (which depend on enums and base classes)
javac -d bin -cp bin src/model/*.java

# 4. Compile repository base classes
javac -d bin -cp bin src/repository/base/*.java

# 5. Compile repository mappers (depends on models)
javac -d bin -cp bin src/repository/mapper/*.java

# 6. Compile repositories (depends on mappers and base repository)
javac -d bin -cp bin src/repository/*.java

# 7. Compile services (depends on repositories and models)
javac -d bin -cp bin src/service/*.java

# 8. Compile UI classes (depends on models)
javac -d bin -cp bin src/ui/*.java

# 9. Compile utilities and validators
javac -d bin -cp bin src/util/*.java
javac -d bin -cp bin src/validator/*.java

# 10. Compile controllers (depends on almost everything)
javac -d bin -cp bin src/controller/*.java

# 11. Finally compile main class
javac -d bin -cp bin src/main/*.java

echo "Compilation complete."
echo "Running the application..."
java -cp bin main.Main