#!/bin/bash -e

##########################################################################
### Deployment Script SSP Designer Integration ###########################
##########################################################################

### CONFIGURATION ########################################################
echo ""
echo "Script Configuration:"
echo "---------------------"

APPNAME="designer"
STAGE="int"
JOB_DIR="/home/carsten/.hudson/jobs/ssp.$APPNAME.export.int/workspace/skysail.product.$APPNAME"
PRODUCT_DIR="/home/carsten/skysail/products/$APPNAME/int"
export JAVA_HOME=/home/carsten/.hudson/tools/hudson.model.JDK/java_SDK_8u25/

echo "APPNAME:     $APPNAME"
echo "STAGE:       $STAGE"
echo "JOB_DIR:     $JOB_DIR"
echo "PRODUCT_DIR: $PRODUCT_DIR"
echo ""

### ZIP ARCHIVE ###########################################################
echo ""
echo "Creating ZIP Archive:"
echo "--------------------"

cd $JOB_DIR/generated/distributions/executable
cp $APPNAME.$STAGE.jar skysail.$APPNAME.jar

zip -r skysail.$APPNAME.zip ../../../config/$STAGE skysail.$APPNAME.jar

echo "copying skysail.designer.zip to public site"
cp skysail.designer.zip /var/www/skysail/products/designer/skysail.designer.int.zip
cp skysail.designer.jar /var/www/skysail/products/designer/skysail.designer.int.jar

mkdir -p /home/carsten/skysail/products/designer/int/bin/designerbundles
mkdir -p /home/carsten/skysail/products/designer/int/lib

echo "copying skysail.designer.jar to products directory"
cp skysail.designer.jar /home/carsten/skysail/products/designer/int/bin/skysail.designer.jar


### STOPPING SERVICE #####################################################
echo ""
echo "Stopping Service:"
echo "-----------------"


if [ -e "$PRODUCT_DIR/bin/$APPNAME_$STAGE" ]
then
  chmod 755 $PRODUCT_DIR/bin/$APPNAME_$STAGE
  $PRODUCT_DIR/bin/$APPNAME_$STAGE stop
fi

cd /home/carsten/.hudson/jobs/ssp.designer.export.int/workspace/skysail.product.designer
cp -r deployment/service/* /home/carsten/skysail/products/designer/int

# needed for designer functionality (to get access to the contained jars for compiling)
#unzip -o /home/carsten/skysail/products/designer/int/bin/skysail.designer.jar

echo "getting config files for installation from svn (and from skysail jar itself)"
cd /home/carsten/skysail/products/designer/int/bin
rm -rf config
mkdir -p config
cd config
svn checkout https://85.25.22.125/repos/skysale/skysailconfigs/designer/int/

#echo "starting designer service"
cd /home/carsten/skysail/products/designer/int/bin/
#rm -rf META-INF
#rm -rf aQute
#rm -rf jar
# not really necessary:
unzip -o skysail.designer.jar
chmod 755 designer_int
$SERVICE start






