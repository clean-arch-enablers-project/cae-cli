::[Bat To Exe Converter]
::
::YAwzoRdxOk+EWAnk
::fBw5plQjdG8=
::YAwzuBVtJxjWCl3EqQJgSA==
::ZR4luwNxJguZRRnk
::Yhs/ulQjdF+5
::cxAkpRVqdFKZSDk=
::cBs/ulQjdF+5
::ZR41oxFsdFKZSDk=
::eBoioBt6dFKZSDk=
::cRo6pxp7LAbNWATEpCI=
::egkzugNsPRvcWATEpCI=
::dAsiuh18IRvcCxnZtBJQ
::cRYluBh/LU+EWAnk
::YxY4rhs+aU+JeA==
::cxY6rQJ7JhzQF1fEqQJQ
::ZQ05rAF9IBncCkqN+0xwdVs0
::ZQ05rAF9IAHYFVzEqQJQ
::eg0/rx1wNQPfEVWB+kM9LVsJDGQ=
::fBEirQZwNQPfEVWB+kM9LVsJDGQ=
::cRolqwZ3JBvQF1fEqQJQ
::dhA7uBVwLU+EWDk=
::YQ03rBFzNR3SWATElA==
::dhAmsQZ3MwfNWATElA==
::ZQ0/vhVqMQ3MEVWAtB9wSA==
::Zg8zqx1/OA3MEVWAtB9wSA==
::dhA7pRFwIByZRRnk
::Zh4grVQjdCyDJGyX8VAjFBFbXwGOAEi7FrwI5+Ty4Nakg2ghV+M6NYzX04iHLvMH60nocIQR1Xtf1cgABVZRcAG/bwM4rHwMs3yAVw==
::YB416Ek+ZG8=
::
::
::978f952a14a936cc963da21a135fa983
@echo off

if exist %USERPROFILE%\cae (
	echo Found the cae directory. Using it.
) else (
	echo Not found cae directory. Creating it.
	mkdir %USERPROFILE%\cae
)
echo @echo off > %USERPROFILE%\cae\cae.bat
echo java -jar %USERPROFILE%\cae\cae-cli.jar %%* >> %USERPROFILE%\cae\cae.bat

setx cae "%USERPROFILE%\cae"
setx CAE_META_STRUCTURE_TEMPLATES_PATH "%USERPROFILE%\cae\file-templates"


echo alias cae='cmd //c cae' >> %USERPROFILE%\.bashrc
echo export CAE_META_STRUCTURE_TEMPLATES_PATH="${CAE_META_STRUCTURE_TEMPLATES_PATH}" >> %USERPROFILE%\.bashrc

copy .\components\cae-cli.jar %USERPROFILE%\cae
xcopy .\components\file-templates %USERPROFILE%\cae\file-templates /E /I /H /Y