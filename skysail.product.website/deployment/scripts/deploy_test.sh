#!/bin/bash -e

export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

cd /home/carsten/.hudson/jobs/skysail.product.website.export.test/workspace/skysail.product.website/generated/distributions/executable

echo "Creating ZIP Archive"
cp test.jar skysail.website.jar
zip -r skysail.website.zip ../../../config/test skysail.website.jar

#echo "copying skysail.website.zip to public site"
#cp skysail.website.zip /var/www/skysail/products/website/skysail.website.zip

mkdir -p /home/carsten/skysail/products/website/test/bin
mkdir -p /home/carsten/skysail/products/website/test/lib

echo "copying skysail.website.jar to products directory"
cp skysail.website.jar /home/carsten/skysail/products/website/test/bin/skysail.website.jar

echo "stopping website service"
/home/carsten/skysail/products/website/test/bin/website_test stop

cd /home/carsten/.hudson/jobs/skysail.product.website.export.test/workspace/skysail.product.website
cp -r deployment/service/* /home/carsten/skysail/products/website/test

# needed for website functionality (to get access to the contained jars for compiling)
unzip -o /home/carsten/skysail/products/website/test/bin/skysail.website.jar

echo "getting config files for installation from svn"
cd /home/carsten/skysail/products/website/test/bin/config
rm -rf test
svn checkout https://85.25.22.125/repos/skysale/skysailconfigs/website/test/

echo "starting website service"
cd /home/carsten/skysail/products/website/test/bin/
rm -rf META-INF
rm -rf aQute
rm -rf jar
unzip -o skysail.website.jar
./website_test start






