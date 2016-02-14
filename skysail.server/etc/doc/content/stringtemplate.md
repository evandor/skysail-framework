title=Second Post
date=2016-01-25
type=post
tags=blog
status=published
~~~~~~


In io.skysail.server.converter.impl.StringTemplateRenderer:

1) createStringTemplateGroup(resource, theme)
   
   * check appBundles /template dir
   * importTemplate("skysail.server.converter", resource, appBundle, "/templates", stGroup, theme);
   
   * importTemplate(productBundleName, resource, appBundle, "/templates", stGroup, theme);
            
