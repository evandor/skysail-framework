#!/bin/bash -e

export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

cd /home/carsten/.hudson/jobs/skysail.product.todos.export.demo/workspace/skysail.product.todos/generated/distributions/executable

#echo "Creating ZIP Archive"
cp todos.demo.jar skysail.todos.jar

zip -r skysail.todos.zip ../../../config/demo skysail.todos.jar
#zip -r skysail.todos.zip skysail.todos.jar

echo "copying skysail.todos.zip to public site"
cp skysail.todos.zip /var/www/skysail/products/todos/skysail.todos.demo.zip
cp skysail.todos.jar /var/www/skysail/products/todos/skysail.todos.demo.jar

mkdir -p /home/carsten/skysail/products/todos/demo/bin
mkdir -p /home/carsten/skysail/products/todos/demo/lib

echo "copying skysail.todos.jar to products directory"
cp skysail.todos.jar /home/carsten/skysail/products/todos/demo/bin/skysail.todos.jar

echo "stopping todos service"
/home/carsten/skysail/products/todos/demo/bin/todos_demo stop

cd /home/carsten/.hudson/jobs/skysail.product.todos.export.demo/workspace/skysail.product.todos
cp -r deployment/service/* /home/carsten/skysail/products/todos/demo

# needed for todos functionality (to get access to the contained jars for compiling)
#unzip -o /home/carsten/skysail/products/todos/demo/bin/skysail.todos.jar

#echo "getting config files for installation from svn"
cd /home/carsten/skysail/products/todos/demo/bin
rm -rf config
mkdir config
cd config
svn checkout https://85.25.22.125/repos/skysale/skysailconfigs/todos/demo/

#echo "starting todos service"
cd /home/carsten/skysail/products/todos/demo/bin/
#rm -rf META-INF
#rm -rf aQute
#rm -rf jar
# not really necessary:
unzip -o skysail.todos.jar
chmod 755 todos_demo
./todos_demo start






