#!/bin/bash -e

export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

cd /home/carsten/.hudson/jobs/skysail.product.todos.export.export/workspace/skysail.product.todos/generated/distributions/executable

#echo "Creating ZIP Archive"
cp todos.runtime.jar skysail.todos.runtime.jar

zip -r skysail.todos.runtime.zip ../../../config/runtime skysail.todos.runtime.jar

echo "copying skysail.todos.runtime.zip to public site"
cp skysail.todos.runtime.zip /var/www/skysail/products/todos/skysail.todos.runtime.zip
