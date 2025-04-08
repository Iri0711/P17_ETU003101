@echo off

:: Variables
set APP_NAME=ETU003101
set SRC_DIR=src\main\java
set WEB_DIR=src\main\webapp
set BUILD_DIR=build
set LIB_DIR=lib
set TOMCAT_WEBAPPS=C:\xampp\tomcat\webapps
set SERVLET_API_JAR=%LIB_DIR%\servlet-api.jar
set JAKARTA_API_JAR=%LIB_DIR%\jakarta.servlet.jsp.jstl-api-2.0.0.jar
set JAKARTA_AP_JAR=%LIB_DIR%\jakarta.servlet.jsp.jstl-2.0.0.jar
set MYSQL_CONNECTOR_JAR=%LIB_DIR%\mysql-connector-j.9.2.0.jar

:: Nettoyage et création du répertoire temporaire
if exist %BUILD_DIR% (
    rmdir /s /q %BUILD_DIR%
)
mkdir %BUILD_DIR%\WEB-INF\classes
mkdir %BUILD_DIR%\WEB-INF\classes\etc\config

 
cmd /c "copy /Y %SRC_DIR%\etc\config\*.properties %BUILD_DIR%\WEB-INF\classes\etc\config\"

:: Compilation des fichiers Java avec le JAR des servlets
dir /b /s %SRC_DIR%\*.java > sources.txt
javac -cp "%SERVLET_API_JAR%;%MYSQL_CONNECTOR_JAR%;%JAKARTA_API_JAR%;%JAKARTA_AP_JAR%" -d %BUILD_DIR%\WEB-INF\classes @sources.txt
del sources.txt

:: Copier les fichiers web (web.xml, JSP, etc.)
xcopy /E /I /Y %WEB_DIR%\* %BUILD_DIR%\

:: Générer le fichier .war dans le dossier build
cd %BUILD_DIR%
jar -cvf %APP_NAME%.war *
cd ..

echo Source: "%BUILD_DIR%\%APP_NAME%.war"
echo Destination: "%TOMCAT_WEBAPPS%"

cmd /c "copy /Y %BUILD_DIR%\%APP_NAME%.war %TOMCAT_WEBAPPS%\"

echo.
echo Déploiement terminé. Redémarrez Tomcat si nécessaire.
echo.
pause
