index(user, messages, source, converter, services, resource, fields, target) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>$messages.("productName")$</title>

	<!-- jQuery -->
	<script type="text/javascript" src="/webapp/js/jquery-1.11.1.js"></script>
	    
    <link href="/webapp/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webapp/css/bootstrap-theme.min.css" rel="stylesheet">
    
    <link rel="stylesheet" href="/webapp/css/select2.css">
    <link rel="stylesheet" href="/webapp/css/select2-bootstrap.css">
    
    <!-- jstree -->
    <link rel="stylesheet" href="/static/themes/jstree/style.min.css" />
    <script src="/static/js/jstree/jstree.min.js"></script>

    <!-- mousetrap -->
    <script src="/static/js/mousetrap/mousetrap.min.js"></script>
    
    <script type="text/javascript">
        Mousetrap.bind('command+s', function(e) {
            alert("keyboard shortcut for save not yet implemented");
        });
    </script>
    
    <script type="text/javascript" src="/webapp/js/select2.js"></script>
	$select2JsHead(fields)$	
    <script type="text/javascript" src="/webapp/js/moment.js"></script>
    $endOfHeaderJS(fields)$
    
  </head>

  <body role="document" style="background-color: #eeeeee;">

    
    <div class="container-fluid">
    
      <div class="row">
        <div class="col-md-2">$appNavTitle(resource)$</div>
        <div class="col-md-8">
            $menuNav(resource.linkheader)$
        </div>
        <div class="col-md-2">&nbsp;</div>
      </div>
$mainContent(user, resource, converter, messages, source, fields)$
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