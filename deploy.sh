# variables
APP_NAME="hello"
SRC_DIR="src/main/java"
WEB_DIR="src/main/webapp"
BUILD_DIR="build"
LIB_DIR="lib"
TOMCAT_WEBAPPS="D:/Apache Software Foundation/Tomcat 10.1/webapps"
SERVLET_API_JAR="$LIB_DIR/servlet-api.jar"

# nettoyage et creation du repertoire temporaire
rm -rf $BUILD_DIR
mkdir -p $BUILD_DIR/WEB-INF/classes

# Compilation des fichiers java avec le jar des servlets
find $SRC_DIR -name "*.java" > sources.txt
javac -cp $SERVLET_API_JAR -d $BUILD_DIR/WEB-INF/classes @sources.txt
rm sources.txt

# Copier les fichiers web (web.xml, JSP, etc.)
cp -r $WEB_DIR/* $BUILD_DIR/

# Generer le fichier .war dans le dossier build
cd $BUILD_DIR || exit
jar -cvf $APP_NAME.war *
cd ..

# Deploiment dans Tomcat
cp -f $BUILD_DIR/$APP_NAME.war/ $TOMCAT_WEBAPPS/

echo ""

echo "Deploiment termine. Redemarrer tomcat si necessaire"

echo ""