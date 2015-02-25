index(user, messages, source, converter, services, resource, fields, target) ::= <<
<!DOCTYPE html>
<html lang="en-gb" dir="ltr">

    <head>
        <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <meta name="description" content="">
	    <meta name="author" content="">
	    <link rel="icon" href="../../favicon.ico">
	
	    <title>Skysail</title>

        <!-- jQuery --> 
		<script type="text/javascript" src="/webapp/js/jquery-1.11.1.js"></script>
	    
	    <link href="/webapp/css/bootstrap.min.css" rel="stylesheet">
    	<link href="/webapp/css/bootstrap-theme.min.css" rel="stylesheet">
    
        $endOfHeaderJS()$
    </head>

    <body role="document">
              
        
        
         <!-- Fixed navbar -->
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="/">Skysail</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            $dropdownFrontendsNav(services)$
            $dropdownApplicationsNav(services)$
            $dropdownAdminNav(services)$
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">View (advanced) <span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li class="divider"></li>
                <li class="dropdown-header">Nav header</li>
                <li><a href='javascript:document.cookie="template=debug;path=/";window.location.reload(true)'>Debug</a></li>
                <li><a href='javascript:document.cookie="template=edit;path=/";window.location.reload(true)'>Edit</a></li>
                <li><a href='javascript:document.cookie="inspect=on;path=/";window.location.reload(true)'>Inspect on</a></li>
                <li><a href='javascript:document.cookie="inspect=off;path=/";window.location.reload(true)'>Inspect off</a></li>
                <li><a href='javascript:document.cookie="template=simple;path=/";window.location.reload(true)'>Simple</a></li>
                <li><a href='javascript:document.cookie="mainpage=tests;path=/";window.location="/_largetests"'>Tests</a></li>
              </ul>
            </li>
            <li>
				<a href="/webapp/impress.html">About...</a>
			</li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li><a href="#">$user.principal$</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>

    <br><br><br>
    
    <div class="container-fluid">
      <div class="col-md-1">&nbsp;</div>	
      <div class="col-md-10">
        <div class="row">
            $breadcrumb(resource)$
        </div>
        <div class="row">
            $menuNav(resource.linkheader)$
        </div>
        <div class="row">
            &nbsp;
        </div>
        <div class="row">
            $if(converter.edit)$
              <a href='/i18n/messages/$resource.classname$.message/'>edit</a>
              $contentHeader(messages)$
            $else$
              $contentHeader(messages)$
            $endif$
        </div>
        <div class="row">
        redirecting to '$source.entity.redirectBackTo$'
		    <form role="form" action=".?method=PUT&_redirectTo=$source.entity.redirectBackTo$" method="POST">
		        <div class="form-group">
		            <label for="exampleInputEmail1">Markdown [<a href='http://daringfireball.net/projects/markdown/syntax' target='_blank'">?</a>] for $first(fields).entity.msgKey$</label>
		            <textarea data-uk-htmleditor="{maxsplitsize:600, markdown:true}" name="msg">$first(fields).entity.msg$</textarea>
		        </div>
		        <button type="submit" class="btn btn-default">Update</button>
		    </form>
	  </div>
      
      $if(converter.debug)$
        $debug(resource, source, target, messages)$
      $endif$ 
      
      <div class="col-md-1">&nbsp;</div>
    </div>


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/webapp/js/bootstrap.min.js"></script>
    $notifications(converter.notifications)$
    </body>

</html>

>>