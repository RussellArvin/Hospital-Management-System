@echo off
echo Creating directories...
mkdir bin 2>nul
mkdir data 2>nul

echo Compiling Java files...

rem 1. First compile enums (since they are referenced by other classes)
javac -d bin src/enums/*.java

rem 2. Compile base models and interfaces
javac -d bin -cp bin src/model/BaseEntity.java
javac -d bin -cp bin src/controller/Controller.java

rem 3. Compile models (which depend on enums and base classes)
javac -d bin -cp bin src/model/*.java

rem 4. Compile repository base classes
javac -d bin -cp bin src/repository/base/*.java

rem 5. Compile repository mappers (depends on models)
javac -d bin -cp bin src/repository/mapper/*.java

rem 6. Compile repositories (depends on mappers and base repository)
javac -d bin -cp bin src/repository/*.java

rem 7. Compile services (depends on repositories and models)
javac -d bin -cp bin src/service/*.java

rem 8. Compile UI classes (depends on models)
javac -d bin -cp bin src/ui/*.java

rem 9. Compile utilities and validators
javac -d bin -cp bin src/util/*.java
javac -d bin -cp bin src/validator/*.java

rem 10. Compile controllers (depends on almost everything)
javac -d bin -cp bin src/controller/*.java

rem 11. Finally compile main class
javac -d bin -cp bin src/main/*.java

echo Compilation complete.
echo Running the application...
java -cp bin main.Main

pause