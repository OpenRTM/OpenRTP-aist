@echo off
rem ---------------------------------------------------------------------------
rem ---------------------------------------------------------------------------
@set /p LINE= < version
@set VERSION=%LINE:~8%
@set PROJECT_VERSION=%VERSION%
@set JARDIR=openrtp_%VERSION%
@set LIBS=-lib ..\lib -lib ..\lib\a4e-2137 -lib ..\lib\a4e-2137\libs -lib %ECLIPSE_HOME%\plugins

rem ---------------------------------------------------------------------------
rem
rem ---------------------------------------------------------------------------
set TARGETS=^
	jp.go.aist.rtm.toolscommon.profiles ^
	jp.go.aist.rtm.toolscommon.profiles.nl1 ^
	jp.go.aist.rtm.toolscommon ^
	jp.go.aist.rtm.toolscommon.nl1 ^
	jp.go.aist.rtm.rtcbuilder ^
	jp.go.aist.rtm.rtcbuilder.nl1 ^
	jp.go.aist.rtm.rtcbuilder.java ^
	jp.go.aist.rtm.rtcbuilder.python ^
	jp.go.aist.rtm.rtcbuilder.lua ^
	jp.go.aist.rtm.rtcbuilder.processing ^
	jp.go.aist.rtm.repositoryView ^
	jp.go.aist.rtm.repositoryView.nl1 ^
	jp.go.aist.rtm.nameserviceview ^
	jp.go.aist.rtm.nameserviceview.nl1 ^
	jp.go.aist.rtm.systemeditor ^
	jp.go.aist.rtm.systemeditor.nl1

@if exist %JARDIR% (
    rd /S /Q %JARDIR%
)
mkdir %JARDIR%

git clone https://github.com/Nobu19800/jp.go.aist.rtm.rtcbuilder.lua
for /D %%p in ( %TARGETS% ) do (
    @set target=%%p
    echo %%p
    cd %%p
    if "%%p" == "jp.go.aist.rtm.toolscommon" (
        call ant buildAll_win %LIBS%
    ) else (
        call ant buildAll %LIBS%
    )
    if ERRORLEVEL 1 goto FAIL
    copy jar\*aist*.jar ..\%JARDIR%
    cd ..
)

echo "Build finished successfully"
@goto END

:FAIL
cd ..
echo "Build failed" %TARGET%

:END
