::ASSEMBLY LINE BUILDER
@echo off
echo Promotion Type? (Choose * for recommended, @ for stable and x for unstable)
set /p PROMOTION=

set /p MODVERSION=<modversion.txt
set /p CurrentBuild=<buildnumber.txt
set /a BUILD_NUMBER=%CurrentBuild%+1
echo %BUILD_NUMBER% >buildnumber.txt

if %PROMOTION%==* (
	echo %MODVERSION% >recommendedversion.txt
	set %PROMOTION% = "!"
)

if %PROMOTION%==* (set /a VString=!)
if %PROMOTION%==@ (set /a VString=@)
if %PROMOTION%==x (set /a VString=Test_)
set FILE_NAME=[MC1.5]%VString%GreaterSecurity_v%MODVERSION%.%BUILD_NUMBER%.jar
set FILE2_NAME=[MC1.5]%VString%ServerSuit_v%MODVERSION%.%BUILD_NUMBER%.jar
set API_NAME=[MC1.5]%VString%GreaterSecurity_v%MODVERSION%.%BUILD_NUMBER%_api.zip

echo Starting to build %FILE_NAME%

::BUILD
runtime\bin\python\python_mcp runtime\recompile.py %*
runtime\bin\python\python_mcp runtime\reobfuscate.py %*

::ZIP-UP
cd reobf\minecraft\
"..\..\..\7za.exe" a "..\..\builds\%FILE_NAME%" "hangcow\greatersecurity"
"..\..\..\7za.exe" a "..\..\builds\%FILE2_NAME%" "hangcow\serversuit"
"..\..\..\7za.exe" a "..\..\builds\%FILE_NAME%" "universalelectricity"
"..\..\..\7za.exe" a "..\..\builds\%FILE2_NAME%" "universalelectricity"
"..\..\..\7za.exe" a "..\..\builds\%FILE_NAME%" "dark"
"..\..\..\7za.exe" a "..\..\builds\%FILE2_NAME%" "dark"
cd ..\..\
cd src\minecraft\
"..\..\..\7za.exe" a "..\..\builds\%FILE_NAME%" "hangcow\greatersecurity\resources"
"..\..\..\7za.exe" a "..\..\builds\%FILE_NAME%" "dark\library\resources"
"..\..\..\7za.exe" a "..\..\builds\%API_NAME%" "hangcow\greatersecurity\api\"
cd ..\..\

::UPDATE INFO FILE
echo %PROMOTION% %FILE_NAME% %API_NAME%>>info.txt

echo Done building %FILE_NAME%

pause