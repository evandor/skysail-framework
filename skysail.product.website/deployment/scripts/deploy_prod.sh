#!/bin/bash -e

export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

cd /home/carsten/.hudson/jobs/skysail.product.website.export.prod/workspace/skysail.products.website/generated/distributions/executable

echo "Creating ZIP Archive"
cp prod.jar skysail.website.jar
zip -r skysail.website.zip ../../../config/prod skysail.website.jar

#echo "copying skysail.website.zip to public site"
#cp skysail.website.zip /var/www/skysail/products/website/skysail.website.zip

mkdir -p /home/carsten/skysail/products/website/prod/bin
mkdir -p /home/carsten/skysail/products/website/prod/lib

echo "copying skysail.website.jar to products directory"
cp skysail.website.jar /home/carsten/skysail/products/website/prod/bin/skysail.website.jar

echo "stopping website service"
/home/carsten/skysail/products/website/prod/bin/website_prod stop

cd /home/carsten/.hudson/jobs/skysail.product.website.export.prod/workspace/skysail.products.website
cp -r deployment/service/* /home/carsten/skysail/products/website/prod

# needed for website functionality (to get access to the contained jars for compiling)
unzip -o /home/carsten/skysail/products/website/prod/bin/skysail.website.jar

echo "getting config files for installation from svn"
cd /home/carsten/skysail/products/website/prod/bin/config
rm -rf prod
svn checkout https://85.25.22.125/repos/skysale/skysailconfigs/website/prod/

echo "starting website service"
cd /home/carsten/skysail/products/website/prod/bin/
rm -rf META-INF
rm -rf aQute
rm -rf jar
unzip -o skysail.website.jar
./website_prod start






