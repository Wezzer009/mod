@echo off
setlocal EnableExtensions
cd /d "%~dp0"

echo === QuartzClimb 26.2 build ===

set "JDK_HOME="
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "JDK_HOME=%JAVA_HOME%"
if not defined JDK_HOME if exist "%~dp0jdk-25.0.4\bin\java.exe" set "JDK_HOME=%~dp0jdk-25.0.4"
if not defined JDK_HOME if exist "%~dp0jdk25\jdk-25.0.4\bin\java.exe" set "JDK_HOME=%~dp0jdk25\jdk-25.0.4"

if not defined JDK_HOME (
  echo JDK 25 not found.
  echo Set JAVA_HOME to JDK 25 and run this file again.
  echo Example:
  echo   set JAVA_HOME=C:\Path\To\jdk-25.0.4
  pause
  exit /b 1
)

"%JDK_HOME%\bin\java.exe" -version
if errorlevel 1 exit /b 1

set "GRADLE_VERSION=9.5.1"
set "GRADLE_DIR=%~dp0.gradle-dist\gradle-%GRADLE_VERSION%"
set "GRADLE_ZIP=%~dp0.gradle-dist\gradle-%GRADLE_VERSION%-bin.zip"

if not exist "%GRADLE_DIR%\bin\gradle.bat" (
  echo Gradle %GRADLE_VERSION% not found locally. Downloading...
  if not exist "%~dp0.gradle-dist" mkdir "%~dp0.gradle-dist"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile '%GRADLE_ZIP%'"
  if errorlevel 1 (
    echo Could not download Gradle.
    pause
    exit /b 1
  )
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%GRADLE_ZIP%' -DestinationPath '%~dp0.gradle-dist' -Force"
  if errorlevel 1 (
    echo Could not extract Gradle.
    pause
    exit /b 1
  )
)

set "JAVA_HOME=%JDK_HOME%"
call "%GRADLE_DIR%\bin\gradle.bat" --no-daemon build
if errorlevel 1 (
  echo.
  echo BUILD FAILED.
  echo Read the error above and send it to ChatGPT if you need the source fixed.
  pause
  exit /b 1
)

echo.
echo BUILD SUCCESSFUL
 echo JAR: %~dp0build\libs\quartzclimb-0.1.0.jar
pause
