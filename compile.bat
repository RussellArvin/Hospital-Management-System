@echo off
echo Creating directories...
if not exist bin mkdir bin
if not exist data mkdir data
if not exist src\repository\mapper mkdir src\repository\mapper

echo Cleaning bin directory...
del /Q /S bin\*

echo Compiling Java files...
set CLASSPATH=bin

:: 1. Compile constants, enums, and base
echo Compiling base files...
javac -d bin src\util\Constant.java
javac -d bin -cp %CLASSPATH% src\enums\*.java
javac -d bin -cp %CLASSPATH% src\model\Entity.java
javac -d bin -cp %CLASSPATH% src\model\BaseEntity.java
javac -d bin -cp %CLASSPATH% src\controller\Controller.java
javac -d bin -cp %CLASSPATH% src\repository\mapper\BaseMapper.java
javac -d bin -cp %CLASSPATH% src\util\*.java
javac -d bin -cp %CLASSPATH% src\validator\*.java

:: 2. Compile all models
echo Compiling models...
javac -d bin -cp %CLASSPATH% src\model\*.java

:: 3. Compile mapper implementations
echo Compiling mappers...
javac -d bin -cp %CLASSPATH% src\repository\mapper\*.java

:: 4. Compile repository base
echo Compiling repository base...
javac -d bin -cp %CLASSPATH% src\repository\base\CsvFileManager.java
javac -d bin -cp %CLASSPATH% src\repository\base\CsvRepository.java

:: 5. Compile repositories
echo Compiling repositories...
javac -d bin -cp %CLASSPATH% src\repository\*.java

:: 6. Compile rest of application
echo Compiling services...
javac -d bin -cp %CLASSPATH% src\service\*.java

echo Compiling UI...
javac -d bin -cp %CLASSPATH% src\ui\*.java

echo Compiling controllers...
javac -d bin -cp %CLASSPATH% src\controller\BaseController.java
javac -d bin -cp %CLASSPATH% src\controller\*.java

echo Compiling main...
javac -d bin -cp %CLASSPATH% src\main\*.java

echo Compilation complete.

:: Check if Main class exists and run
if exist bin\main\Main.class (
    echo Running the application...
    java -cp bin main.Main
) else (
    echo Error: Main class not found!
    exit /b 1
)
