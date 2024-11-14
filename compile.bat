@echo off
echo Creating directories...
mkdir bin 2>nul
mkdir data 2>nul

echo Compiling Java files...

rem 1. Compile enums (since they are referenced by other classes)
javac -d bin src/enums/*.java

rem 2. Compile base models and interfaces (e.g., Entity and BaseEntity)
javac -d bin -cp bin src/model/Entity.java
javac -d bin -cp bin src/model/BaseEntity.java

rem 3. Compile controller base class
javac -d bin -cp bin src/controller/Controller.java

rem 4. Compile models (which depend on enums and base classes)
javac -d bin -cp bin src/model/*.java

rem 5. Compile repository base classes
javac -d bin -cp bin src/repository/base/*.java

rem 6. Compile repository mappers (depends on models)
javac -d bin -cp bin src/repository/mapper/*.java

rem 7. Compile repositories (depends on mappers and base repository)
javac -d bin -cp bin src/repository/*.java

rem 8. Compile services (depends on repositories and models)
javac -d bin -cp bin src/service/*.java

rem 9. Compile UI classes (depends on models)
javac -d bin -cp bin src/ui/*.java

rem 10. Compile utilities and validators
javac -d bin -cp bin src/util/*.java
javac -d bin -cp bin src/validator/*.java

rem 11. Compile controllers (depends on almost everything)
javac -d bin -cp bin src/controller/*.java

rem 12. Finally compile main class
javac -d bin -cp bin src/main/*.java

echo Compilation complete.
echo Running the application...
java -cp bin main.Main

pause
