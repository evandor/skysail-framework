#!/bin/bash -e

cd /home/carsten/.hudson/jobs/skysail.product.website.export.zip/workspace/skysail.products.website/generated/distributions/executable

echo "Creating ZIP Archive"
cp zip.jar skysail.website.jar
mkdir config
cp -r ../../../config/zip/* config
cp ../../../config/README.md .
zip -r skysail.website.zip config README.md skysail.website.jar

echo "copying skysail.website.zip to public site"
cp skysail.website.zip /var/www/skysail/products/website/skysail.website.zip


