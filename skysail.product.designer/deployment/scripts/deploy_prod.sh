#!/bin/bash -e

##########################################################################
### Deployment Script SSP Desinger Integration ###########################
##########################################################################

### CONFIGURATION ########################################################
echo ""
echo "Script Configuration:"
echo "---------------------"

APPNAME="designer"
STAGE="prod"

MAIN_DEPLOY_SCRIPT=../../../skysail.server/deployment/scripts/deploy.sh
echo "calling $MAIN_DEPLOY_SCRIPT"

chmod 775 $MAIN_DEPLOY_SCRIPT
source $MAIN_DEPLOY_SCRIPT

