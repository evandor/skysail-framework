#!/bin/bash -e

APPNAME="designer"

export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

cd /home/carsten/.hudson/jobs/ssp.$APPNAME.export.int/workspace/skysail.product.$APPNAME/generated/distributions/executable

echo "Creating ZIP Archive"
cp $APPNAME.int.jar skysail.$APPNAME.jar

zip -r skysail.$APPNAME.zip ../../../config/int skysail.$APPNAME.jar
#zip -r skysail.$APPNAME.zip skysail.$APPNAME.jar

echo "copying skysail.$APPNAME.zip to public site"
cp skysail.$APPNAME.zip /var/www/skysail/products/$APPNAME/skysail.$APPNAME.int.zip
cp skysail.$APPNAME.jar /var/www/skysail/products/$APPNAME/skysail.$APPNAME.int.jar

mkdir -p /home/carsten/skysail/products/$APPNAME/int/bin
mkdir -p /home/carsten/skysail/products/$APPNAME/int/lib

echo "copying skysail.$APPNAME.jar to products directory"
cp skysail.$APPNAME.jar /home/carsten/skysail/products/$APPNAME/int/bin/skysail.$APPNAME.jar

echo "stopping $APPNAME service"
#/home/carsten/skysail/products/$APPNAME/int/bin/$APPNAME_int stop

cd /home/carsten/.hudson/jobs/ssp.$APPNAME.export.int/workspace/skysail.product.$APPNAME
cp -r deployment/service/* /home/carsten/skysail/products/$APPNAME/int

# needed for $APPNAME functionality (to get access to the contained jars for compiling)
#unzip -o /home/carsten/skysail/products/$APPNAME/int/bin/skysail.$APPNAME.jar

#echo "getting config files for installation from svn"
cd /home/carsten/skysail/products/$APPNAME/int/bin
rm -rf config
mkdir config
cd config
#svn checkout https://85.25.22.125/repos/skysale/skysailconfigs/$APPNAME/int/

#echo "starting $APPNAME service"
cd /home/carsten/skysail/products/$APPNAME/int/bin/
#rm -rf META-INF
#rm -rf aQute
#rm -rf jar
# not really necessary:
unzip -o skysail.$APPNAME.jar
chmod 755 $APPNAME_int
./$APPNAME_int start






