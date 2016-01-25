#!/bin/bash -e

### config ###

APPNAME="designer"
export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

cd /home/carsten/.hudson/jobs/ssp.designer.export.prod/workspace/skysail.product.designer/generated/distributions/executable
echo "Creating ZIP Archive"
cp designer.prod.jar skysail.designer.jar

zip -r skysail.designer.zip ../../../config/prod skysail.designer.jar
#zip -r skysail.designer.zip skysail.designer.jar

echo "copying skysail.designer.zip to public site"
cp skysail.designer.zip /var/www/skysail/products/designer/skysail.designer.prod.zip
cp skysail.designer.jar /var/www/skysail/products/designer/skysail.designer.prod.jar

mkdir -p /home/carsten/skysail/products/designer/prod/bin/designerbundles
mkdir -p /home/carsten/skysail/products/designer/prod/lib

echo "copying skysail.designer.jar to products directory"
cp skysail.designer.jar /home/carsten/skysail/products/designer/prod/bin/skysail.designer.jar

echo "stopping designer service"
/home/carsten/skysail/products/designer/prod/bin/designer_prod stop

cd /home/carsten/.hudson/jobs/ssp.designer.export.prod/workspace/skysail.product.designer
cp -r deployment/service/* /home/carsten/skysail/products/designer/prod

# needed for designer functionality (to get access to the contained jars for compiling)
#unzip -o /home/carsten/skysail/products/designer/prod/bin/skysail.designer.jar

#echo "getting config files for installation from svn"
cd /home/carsten/skysail/products/designer/prod/bin
#rm -rf config
#mkdir config
cd config
svn checkout https://85.25.22.125/repos/skysale/skysailconfigs/designer/prod/

#echo "starting designer service"
cd /home/carsten/skysail/products/designer/prod/bin/
#rm -rf META-INF
#rm -rf aQute
#rm -rf jar
# not really necessary:
unzip -o skysail.designer.jar
chmod 755 designer_prod
./designer_prod start






