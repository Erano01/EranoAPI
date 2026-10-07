@echo off
rem Builds one Spigot server jar per NMS revision (1.8 -> 26.3) with BuildTools and
rem installs them into the local Maven repository (%USERPROFILE%\.m2).
rem Linux counterpart: build-spigot-jars.sh (same version table, see dependencies.md).
rem
rem Usage:
rem   build-spigot-jars.bat                 build everything that is missing
rem   build-spigot-jars.bat 1.20.4 26.3     build only these versions
rem
rem Environment:
rem   BUILDTOOLS_DIR   BuildTools directory (default: %USERPROFILE%\MinecraftWorkspace\Buildtools)
rem   JAVA8_HOME, JAVA11_HOME, JAVA17_HOME, JAVA21_HOME, JAVA25_HOME
rem                    JDK to use (default: C:\Program Files\Java\jdk-1.8, jdk-11, jdk-17, ...)
rem   FORCE=1          rebuild even if the jar already exists

setlocal EnableDelayedExpansion
title SpigotMC BuildTools Builder

rem <minecraft version>:<java>:<remapped>
set VERSIONS=1.8:8:0 1.8.3:8:0 1.8.8:8:0 1.9.2:8:0 1.9.4:8:0 1.10.2:8:0 1.11.2:8:0 1.12.2:8:0 ^
 1.13:11:0 1.13.2:11:0 1.14.4:11:0 1.15.2:11:0 1.16.1:11:0 1.16.3:11:0 1.16.5:11:0 ^
 1.17.1:17:1 1.18.1:17:1 1.18.2:17:1 1.19.2:17:1 1.19.3:17:1 1.19.4:17:1 1.20.1:17:1 1.20.2:17:1 1.20.4:17:1 ^
 1.20.6:21:1 1.21.1:21:1 1.21.3:21:1 1.21.4:21:1 1.21.5:21:1 1.21.8:21:1 1.21.10:21:1 1.21.11:21:1 ^
 26.1.2:25:0 26.2:25:0 26.3:25:0

if not defined BUILDTOOLS_DIR set "BUILDTOOLS_DIR=%USERPROFILE%\MinecraftWorkspace\Buildtools"
if not defined JAVA8_HOME  set "JAVA8_HOME=C:\Program Files\Java\jdk-1.8"
if not defined JAVA11_HOME set "JAVA11_HOME=C:\Program Files\Java\jdk-11"
if not defined JAVA17_HOME set "JAVA17_HOME=C:\Program Files\Java\jdk-17"
if not defined JAVA21_HOME set "JAVA21_HOME=C:\Program Files\Java\jdk-21"
if not defined JAVA25_HOME set "JAVA25_HOME=C:\Program Files\Java\jdk-25"

if not exist "%BUILDTOOLS_DIR%\logs" mkdir "%BUILDTOOLS_DIR%\logs"
cd /d "%BUILDTOOLS_DIR%"

rem oss.sonatype.org is shut down; old versions (1.8 - 1.15) still reference it.
rem Redirect it to Spigot's public repo through a *global* settings file.
(
echo ^<settings^>^<mirrors^>^<mirror^>
echo   ^<id^>spigot-public^</id^>
echo   ^<mirrorOf^>sonatype-nexus-snapshots,sonatype-nexus-releases,sonatype,oss-sonatype^</mirrorOf^>
echo   ^<url^>https://hub.spigotmc.org/nexus/content/groups/public/^</url^>
echo ^</mirror^>^</mirrors^>^</settings^>
) > maven-settings.xml
set "MAVEN_ARGS=-gs %BUILDTOOLS_DIR%\maven-settings.xml"

echo ==^> Updating BuildTools in %BUILDTOOLS_DIR%
curl -fsSL -z BuildTools.jar -o BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
if errorlevel 1 (
    echo xx Could not download BuildTools
    exit /b 1
)

set BUILT=
set SKIPPED=
set FAILED=

for %%E in (%VERSIONS%) do (
    for /f "tokens=1-3 delims=:" %%a in ("%%E") do (
        set "REV=%%a" & set "JAVA=%%b" & set "REMAP=%%c"
        set WANTED=1
        if not "%~1"=="" (
            set WANTED=0
            for %%A in (%*) do if "%%A"=="%%a" set WANTED=1
        )
        if "!WANTED!"=="1" call :build
    )
)

echo.
echo ==^> Built:  !BUILT!
echo ==^> Skipped:!SKIPPED!
if defined FAILED (
    echo WARN: Failed: !FAILED!
    pause
    exit /b 1
)
echo ==^> Jars: %BUILDTOOLS_DIR%  (also installed into %USERPROFILE%\.m2)
pause
exit /b 0

:build
if exist "spigot-%REV%.jar" if not "%FORCE%"=="1" (
    echo ==^> %REV% already built, skipping
    set "SKIPPED=!SKIPPED! %REV%"
    exit /b 0
)
set "JDK=!JAVA%JAVA%_HOME!"
if not exist "!JDK!\bin\java.exe" (
    echo WARN: %REV% needs Java %JAVA%, not found at "!JDK!" ^(set JAVA%JAVA%_HOME^), skipping
    set "FAILED=!FAILED! %REV%"
    exit /b 0
)
set "ARGS=--rev %REV% --nogui"
if "%REMAP%"=="1" set "ARGS=!ARGS! --remapped"
echo ==^> %REV% with Java %JAVA% -^> log: logs\%REV%.log
"!JDK!\bin\java.exe" -jar BuildTools.jar !ARGS! > "logs\%REV%.log" 2>&1
if errorlevel 1 (
    echo WARN: %REV% failed, see logs\%REV%.log
    set "FAILED=!FAILED! %REV%"
) else (
    set "BUILT=!BUILT! %REV%"
)
exit /b 0
