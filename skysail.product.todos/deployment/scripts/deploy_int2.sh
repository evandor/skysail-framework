#!/bin/bash -e

export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

JOB_DIR="/home/carsten/.hudson/jobs/ssp.todos.export.int/workspace/skysail.product.todos"
PRODUCT_DIR="/home/carsten/skysail/products/todos"

cd $JOB_DIR/generated/distributions/executable

echo "renaming skysail executable..."
cp todos.int2.jar skysail.todos.jar

echo "copying skysail.todos.jar to public site"
cp skysail.todos.jar /var/www/skysail/products/todos/skysail.todos.int2.jar

mkdir -p $PRODUCT_DIR/int2/bin/config/int
mkdir -p $PRODUCT_DIR/int2/lib

echo "copying skysail.todos.jar to products directory"
cp skysail.todos.jar $PRODUCT_DIR/int2/bin/skysail.todos.jar

echo "stopping todos service: $PRODUCT_DIR/int2/bin/todos_int2"
if [ -e "$PRODUCT_DIR/int2/bin/todos_int2" ]
then
  chmod 755 $PRODUCT_DIR/int2/bin/todos_int2
  $PRODUCT_DIR/int2/bin/todos_int2 stop
fi


cd $JOB_DIR
cp -r deployment/service/* $PRODUCT_DIR/int2
cp config/int/* $PRODUCT_DIR/int2/bin/config/int


echo "getting config files for installation from svn"
cd $PRODUCT_DIR/int2/bin
#rm -rf config
#mkdir config
cd config
svn export --force https://85.25.22.125/repos/skysale/skysailconfigs/todos/int/

echo "starting todos service"
cd $PRODUCT_DIR/int2/bin/
#unzip -o skysail.todos.jar
chmod 755 todos_int2
./todos_int2 start






