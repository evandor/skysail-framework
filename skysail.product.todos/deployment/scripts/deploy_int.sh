#!/bin/bash -e

##########################################################################
### Deployment Script SSP Todos Integration ##############################
##########################################################################

### CONFIGURATION ########################################################
echo ""
echo "Script Configuration:"
echo "---------------------"

APPNAME="todos"
STAGE="int"
JOB_DIR="/home/carsten/.hudson/jobs/ssp.$APPNAME.export.int/workspace/skysail.product.$APPNAME"
PRODUCT_DIR="/home/carsten/skysail/products/$APPNAME/$STAGE"
SERVICENAME=$APPNAME_$STAGE
export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

echo "APPNAME:     $APPNAME"
echo "STAGE:       $STAGE"
echo "JOB_DIR:     $JOB_DIR"
echo "PRODUCT_DIR: $PRODUCT_DIR"
echo "SERVICENAME: $SERVICENAME"

### DIRECTORY MANAGEMENT #################################################
echo ""
echo "Setting up directory structures:"
echo "--------------------------------"

mkdir -p $PRODUCT_DIR/bin/config/int
mkdir -p $PRODUCT_DIR/lib

### ZIP ARCHIVE ##########################################################
echo ""
echo "Creating ZIP Archive:"
echo "--------------------"

cd $JOB_DIR/generated/distributions/executable
cp $APPNAME.$STAGE.jar skysail.$APPNAME.jar

zip -r skysail.$APPNAME.zip ../../../config/$STAGE skysail.$APPNAME.jar

### PUBLIC SITE ##########################################################
echo ""
echo "Manage public site:"
echo "-------------------"

echo "copying skysail.$APPNAME.zip to public site"
cp skysail.$APPNAME.zip /var/www/skysail/products/$APPNAME/skysail.$APPNAME.$STAGE.zip
cp skysail.$APPNAME.jar /var/www/skysail/products/$APPNAME/skysail.$APPNAME.$STAGE.jar

### STOPPING SERVICE #####################################################
echo ""
echo "Stopping Service:"
echo "-----------------"

if [ -e "$PRODUCT_DIR/bin/$SERVICENAME" ]
then
  chmod 755 $PRODUCT_DIR/bin/$SERVICENAME
  $PRODUCT_DIR/bin/$SERVICENAME stop
  echo "service was stopped"
else 
  echo "service not yet set up"
fi

### PREPARING SERVICE ####################################################
echo ""
echo "Preparing Service:"
echo "------------------"

echo "copying skysail.$APPNAME.jar to $PRODUCT_DIR/bin/skysail.$APPNAME.jar"
cp skysail.$APPNAME.jar $PRODUCT_DIR/bin/skysail.$APPNAME.jar

cd $JOB_DIR
echo "copying deployment/service/* to $PRODUCT_DIR"
cp -r deployment/service/* $PRODUCT_DIR
echo "copying config/ing/* to $PRODUCT_DIR/bin/config/int"
cp config/$STAGE/* $PRODUCT_DIR/bin/config/$STAGE

### UPDATING CONFIG FROM SVN REP #########################################
echo ""
echo "Updating config from SVN:"
echo "-------------------------"

cd $PRODUCT_DIR/bin
rm -rf config
mkdir -p config
cd config
svn export --force https://85.25.22.125/repos/skysale/skysailconfigs/$APPNAME/$STAGE/

### STARTING SERVICE #####################################################
echo ""
echo "Starting Service:"
echo "----------------"

cd $PRODUCT_DIR/bin/
unzip -o skysail.$APPNAME.jar
chmod 755 $SERVICENAME
./$SERVICENAME start

