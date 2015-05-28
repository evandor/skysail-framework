indexMobile(user, messages, source, converter, services, resource, fields, target) ::= <<
<html class="retina watch-active-state">
<head>
  <meta charset="utf-8">
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui" name="viewport">
  <meta content="yes" name="apple-mobile-web-app-capable">
  <title>Skysail Mobile</title>
  <link type="text/css" rel="stylesheet" media="all" href="/webapp/css/canJsMain.css">
</head>

<body class="">

  <div class="statusbar-overlay"></div>
  <div class="panel-overlay"></div>
  <div class="appmenu-overlay open"></div>
  <div class="appmenu"></div>
  <div class="views tabs toolbar-through">
    <div class="view view-main" data-page="">
      <div class="navbar">
      $menuNavMobile()$
      </div>
      
      <!-- http://www.idangero.us/framework7/apps/ -->
      <div class="pages navbar-through toolbar-through">
        <div class="page page-on-center" data-page="">
          <div class="page-content page_list_page controller">

$mainContentMobile(user, resource, converter, messages, source, fields)$

          </div>
        </div>
      </div>
    </div>
  </div>
  <div style="display: none;" class="popup">
    <div class="view ">
      <div class="navbar"></div>
      <div class="pages navbar-through toolbar-through"></div>
    </div>
  </div>
</body>
</html>
>>
