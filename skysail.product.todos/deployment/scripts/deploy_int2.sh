#!/bin/bash -e

export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

cd /home/carsten/.hudson/jobs/ssp.todos.export.int/workspace/skysail.product.todos/generated/distributions/executable

echo "renaming skysail executable..."
cp todos.int2.jar skysail.todos.jar

echo "copying skysail.todos.jar to public site"
cp skysail.todos.jar /var/www/skysail/products/todos/skysail.todos.int2.jar

mkdir -p /home/carsten/skysail/products/todos/int2/bin
mkdir -p /home/carsten/skysail/products/todos/int2/lib

echo "copying skysail.todos.jar to products directory"
cp skysail.todos.jar /home/carsten/skysail/products/todos/int2/bin/skysail.todos.jar

echo "stopping todos service"
/home/carsten/skysail/products/todos/int2/bin/todos_int2 stop

cd /home/carsten/.hudson/jobs/ssp.todos.export.int/workspace/skysail.product.todos
cp -r deployment/service/* /home/carsten/skysail/products/todos/int2


#echo "getting config files for installation from svn"
#cd /home/carsten/skysail/products/todos/int/bin
#rm -rf config
#mkdir config
#cd config
#svn checkout https://85.25.22.125/repos/skysale/skysailconfigs/todos/int/

#echo "starting todos service"
cd /home/carsten/skysail/products/todos/int2/bin/
unzip -o skysail.todos.jar
chmod 755 todos_int2
./todos_int2 start






