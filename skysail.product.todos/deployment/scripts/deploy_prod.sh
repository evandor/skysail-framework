#!/bin/bash -e

export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

cd /home/carsten/.hudson/jobs/skysail.product.todos.export.prod/workspace/skysail.product.todos/generated/distributions/executable

#echo "Creating ZIP Archive"
cp prod.jar skysail.todos.jar
#zip -r skysail.todos.zip ../../../config/prod skysail.todos.jar
zip -r skysail.todos.zip skysail.todos.jar

echo "copying skysail.todos.zip to public site"
cp skysail.todos.zip /var/www/skysail/products/todos/skysail.todos.zip

mkdir -p /home/carsten/skysail/products/todos/prod/bin
mkdir -p /home/carsten/skysail/products/todos/prod/lib

echo "copying skysail.todos.jar to products directory"
cp skysail.todos.jar /home/carsten/skysail/products/todos/prod/bin/skysail.todos.jar

echo "stopping todos service"
#/home/carsten/skysail/products/todos/prod/bin/todos_prod stop

cd /home/carsten/.hudson/jobs/skysail.product.todos.export.prod/workspace/skysail.product.todos
cp -r deployment/service/* /home/carsten/skysail/products/todos/prod

# needed for todos functionality (to get access to the contained jars for compiling)
#unzip -o /home/carsten/skysail/products/todos/prod/bin/skysail.todos.jar

#echo "getting config files for installation from svn"
#cd /home/carsten/skysail/products/todos/prod/bin/config
#rm -rf prod
#svn checkout https://85.25.22.125/repos/skysale/skysailconfigs/todos/prod/

#echo "starting todos service"
cd /home/carsten/skysail/products/todos/prod/bin/
#rm -rf META-INF
#rm -rf aQute
#rm -rf jar
# not really necessary:
unzip -o skysail.todos.jar
./todos_prod start






