#!/bin/bash -e

### Deployment Script SSP Todos Integration ##############################

echo ""
echo "Configuration:"
echo "--------------"

APPNAME="todos"
JOB_DIR="/home/carsten/.hudson/jobs/ssp.$APPNAME.export.int/workspace/skysail.product.$APPNAME"
PRODUCT_DIR="/home/carsten/skysail/products/$APPNAME/int2"
export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

echo "APPNAME:     $APPNAME"
echo "JOB_DIR:     $JOB_DIR"
echo "PRODUCT_DIR: $PRODUCT_DIR"
echo ""

cd $JOB_DIR/generated/distributions/executable
echo "Creating ZIP Archive"
cp todos.int2.jar skysail.todos.jar

echo "creating directories if not-existing"
echo "------------------------------------"
mkdir -p $PRODUCT_DIR/bin/config/int
mkdir -p $PRODUCT_DIR/lib

echo "copying skysail.todos.jar to products directory"
echo "-----------------------------------------------"
cp skysail.todos.jar $PRODUCT_DIR/bin/skysail.todos.jar

echo "stopping todos service: $PRODUCT_DIR/bin/todos_int2"
echo "---------------------------------------------------"
if [ -e "$PRODUCT_DIR/bin/todos_int2" ]
then
  chmod 755 $PRODUCT_DIR/bin/todos_int2
  $PRODUCT_DIR/bin/todos_int2 stop
fi

cd $JOB_DIR
cp -r deployment/service/* $PRODUCT_DIR
cp config/int/* $PRODUCT_DIR/bin/config/int

echo "getting config files for installation from svn"
echo "----------------------------------------------"
cd $PRODUCT_DIR/bin
rm -rf config
mkdir -p config
cd config
svn export --force https://85.25.22.125/repos/skysale/skysailconfigs/todos/int/

echo "starting todos service"
echo "----------------------"
cd $PRODUCT_DIR/bin/
unzip -o skysail.todos.jar
chmod 755 todos_int2
./todos_int2 start

