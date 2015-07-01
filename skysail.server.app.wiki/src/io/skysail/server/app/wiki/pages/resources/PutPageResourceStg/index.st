index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html>
  <head>
      <meta charset="utf-8">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <meta name="description" content="">
      <meta name="author" content="">

      <title>$messages.("productName")$</title>

      <!-- jQuery -->
      <script type="text/javascript" src="/webapp/js/jquery-1.11.1.js"></script>

      <!-- bootstrap -->
      <link href="/webapp/css/bootstrap.min.css" rel="stylesheet">
      <link href="/webapp/css/stickyfooter.css" rel="stylesheet">
      <link href="/webapp/css/bootstrap-theme.min.css" rel="stylesheet">

      <!-- polymer stuff -->
      <script src="/_polymer/bower_components/webcomponentsjs/webcomponents-lite.min.js"></script>
      <link rel="import" href="/_polymer/proto-element.html">
      <link rel="import" href="/_polymer/sky-app-nav-accordion.html">
      <link rel="import" href="/_polymer/sky-ajax-get.html">
  </head>
  
  <body>

 <!-- Fixed navbar -->
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container" style="margin-left:inherit; padding-left:19px;padding-right:15px;width:100%;">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="/">$messages.("productName")$</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            $dropdownFrontendsNav(model.services)$
            $dropdownApplicationsNav(model.services)$
            $dropdownDesignerNav(model.services)$
            $dropdownAdminNav(model.services)$
            $if(user.developer)$
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">View (advanced) <span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li class="divider"></li>
                <li class="dropdown-header">Nav header</li>
                <li><a href='javascript:document.cookie="template=debug;path=/";window.location.reload(true)'>Debug</a></li>
                <li><a href='javascript:document.cookie="template=edit;path=/";window.location.reload(true)'>Edit</a></li>
                <li><a href='javascript:document.cookie="template=simple;path=/";window.location.reload(true)'>Simple</a></li>
                <li><a href='javascript:inspect()'>Inspect Page</a></li>
                <li><a href='javascript:document.cookie="mainpage=tests;path=/";window.location="/_largetests"'>Tests</a></li>
                <li><a href='javascript:document.cookie="mainpage=indexMobile;path=/";window.location.reload(true)'>Mobile View</a></li>
              </ul>
            </li>
            $endif$
          </ul>
          
          $if(model.services.searchService)$
      <div class="col-sm-3 col-md-2 pull-right">
      <form class="navbar-form" role="search" action="/_search" method="POST">
        <div class="input-group">
          <input type="text" class="form-control input-sm" placeholder="Search" name="searchterm" id="searchterm">
          <div class="input-group-btn">
            <button class="btn btn-default btn-sm" type="submit"><i class="glyphicon glyphicon-search"></i></button>
          </div>
        </div>
      </form>
        </div>
          $endif$
          $if(user.principal)$
          <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">$user.username$ <font style="color:red">$user.backend$</font><span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li><a href='/_logout?targetUri=/'>Logout</a></li>
                <li><a href='/_profile'>Profile</a></li>
                $backends(user)$
              </ul>
            </li>
          </ul>
          $endif$
        </div><!--/.nav-collapse -->
      </div>
    </div>

    <br><br><br>
    
    <div class="container-fluid">
    
      <div class="row">
        <div class="col-md-10">
            $breadcrumb(model)$
        </div>
        <div class="col-md-2">
            $representations(model)$
        </div>
      </div>

$mainContent(user, converter, messages, model)$
      
      $if(converter.debug)$
        $debug(messages, model)$
      $endif$ 
      
    </div>
    
    <footer class="footer">
      <div class="container4footer">
        <p class="text-muted"><div id="output"></div></p>
      </div>
    </footer>

    

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/webapp/js/bootstrap.min.js"></script>
    
  </body>  
</html>
>>