indexMobile(user, messages, source, converter, services, resource, fields, target) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    $resource.metaRefresh$
<!--    <link rel="icon" href="../../favicon.ico">-->

    <title>$messages.("productName")$</title>

	<!-- jQuery -->
	<script type="text/javascript" src="/webapp/js/jquery-1.11.1.js"></script>
	    
    <link href="/webapp/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webapp/css/bootstrap-theme.min.css" rel="stylesheet">
    
    <link rel="stylesheet" href="/webapp/css/select2.css">
    <link rel="stylesheet" href="/webapp/css/select2-bootstrap.css">
    
    <link rel="stylesheet" href="/webapp/js/animate/animate.css">
    
    <!-- jstree -->
    <link rel="stylesheet" href="/static/themes/jstree/style.min.css" />
    <script src="/static/js/jstree/jstree.min.js"></script>
    
    <script type="text/javascript">
        function inspect() {
        	var url = location.href;    
        	if (url.indexOf('?') !== -1){
        		url = (url + "&_inspect=true");
        	} else {
        		url = (url + "?_inspect=true");
        	}
        	location.replace(url);
        	//location.reload(true);
        }
    </script>
    
    <script type="text/javascript" src="/webapp/js/select2.js"></script>
	$select2JsHead(fields)$	
    <script type="text/javascript" src="/webapp/js/moment.js"></script>
    $endOfHeaderJS(fields)$
    
  </head>

  <body role="document">

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
            $dropdownApplicationsNav(services)$
          </ul>
          
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
        <div class="col-md-12">
            $menuNav(resource.links)$
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
            <hr>
        </div>
      </div>
      
$mainContentMobile(user, resource, converter, messages, source, fields)$
      
      $if(converter.debug)$
        $debug(resource, source, target, messages)$
      $endif$ 
      
    </div>


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/webapp/js/bootstrap.min.js"></script>
    $notifications(converter.notifications)$
    $select2JsEndOfBody(fields)$ 
    
  </body>
</html>

>>