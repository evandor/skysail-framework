title=Second Post
date=2016-01-25
type=post
tags=blog
status=published
~~~~~~


In io.skysail.server.converter.impl.StringTemplateRenderer:

1) createStringTemplateGroup(resource, theme)

   Create list of bundle directories to check for string template files:
   
   * add current bundles /template dir
   
   * import templates from skysail.server.converter: '/templates/<theme>' 
   * import templates from skysail.server.converter: '/templates/<theme>/head' 
   * import templates from skysail.server.converter: '/templates/<theme>/navigation'
   * import templates from skysail.server.converter: '/templates/common'
   * import templates from skysail.server.converter: '/templates/common/head'
   * import templates from skysail.server.converter: '/templates/common/navigation'
   
   * import templates from <productBundle>: '/templates/<theme>' 
   * import templates from <productBundle>: '/templates/<theme>/head' 
   * import templates from <productBundle>: '/templates/<theme>/navigation'
   * import templates from <productBundle>: '/templates/common'
   * import templates from <productBundle>: '/templates/common/head'
   * import templates from <productBundle>: '/templates/common/navigation'
   
2) find index (i.e. root) file

   * load(/index): first in io/skysail/server/app/todos/todos/resources/Top10TodosResourceStg//index, 
