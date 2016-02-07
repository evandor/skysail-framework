#!/bin/bash -e

##########################################################################
### Deployment Script SSP Todos Integration ##############################
##########################################################################

### CONFIGURATION ########################################################
echo ""
echo "Script Configuration:"
echo "---------------------"

APPNAME="todos"
STAGE="int"
JOB_DIR="/home/carsten/.hudson/jobs/ssp.$APPNAME.export.int/workspace/skysail.product.$APPNAME"

echo "calling ${JOB_DIR}../skysail.server/deployment/scripts/deploy.sh"
source ${JOB_DIR}../skysail.server/deployment/scripts/deploy.sh

