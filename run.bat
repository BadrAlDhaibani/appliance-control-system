@echo off
echo Smart Home Control System
echo Compiling Java files...

cd src
javac *.java

if %errorlevel% equ 0 (
    echo Compilation successful!
    echo Starting Smart Home Dashboard...
    java Dashboard
) else (
    echo Compilation failed! Please check for errors.
    pause
)

cd ..
pause