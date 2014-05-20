@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Derived from:
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################
@rem
@rem
@rem We have to check these variations:
@rem   - start from current working directory:
@rem        "xxx.bat"
@rem   - start from current working directory without specifying the extension:
@rem        "xxx"
@rem   - start with directory spec:
@rem        "bin\xxx.bat"
@rem   - start with directory spec without extension:
@rem        "bin\xxx"
@rem   - start via PATH:
@rem        "set PATH=bin;%PATH%"
@rem        "xxx.bat"
@rem   - start via PATH without extension:
@rem        "set PATH=bin;%PATH%"
@rem        "xxx"
@rem

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windowz variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set JAR_NAME=%0
call :findJar JAR_NAME %0
::if not exist %JAR_NAME% set JAR_NAME=%0.bat

"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% -classpath "%CLASSPATH%" -jar %JAR_NAME% %CMD_LINE_ARGS% & exit /B

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable GRADLE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%GRADLE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
exit /B

:findJar
setlocal
:: Check for file existence first
set J=%2
if exist %J% goto :finish
:: Append .bat and check for existence
set J=%2.bat
if exist %J% goto :finish
:: Search PATH
for %%i in (%2) do set J=%%~$PATH:i
if not ".%J%" == "." goto :finish
for %%i in (%2.bat) do set J=%%~$PATH:i
if not ".%J%" == "." goto :finish
set J=%2

:finish
(
  endlocal
  set "%~1=%J%"
)
goto:eof

EOF
:: DO NOT DELETE THIS LINE AND THE NEXT CHARACTER!
