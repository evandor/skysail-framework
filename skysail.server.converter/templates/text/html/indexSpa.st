indexSpa(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>$messages.("productName")$ (SPA)</title>

	<!-- jQuery -->
	<script type="text/javascript" src="/webapp/js/jquery-1.11.1.js"></script>
    <script tpye="text/javascript" src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://code.jquery.com/ui/1.11.4/themes/ui-lightness/jquery-ui.css">

    <link href="/webapp/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webapp/css/stickyfooter.css" rel="stylesheet">
    <link href="/webapp/css/bootstrap-theme.min.css" rel="stylesheet">

    <!-- polymer stuff -->
    <script src="/_polymer/bower_components/webcomponentsjs/webcomponents-lite.min.js"></script>
    <link rel="import" href="/_polymer/proto-element.html">
    <link rel="import" href="/_polymer/sky-app-nav-accordion.html">
    <link rel="import" href="/_polymer/sky-ajax-get.html">
    <link rel="import" href="/_polymer/sky-ajax-table.html">
    <link rel="import" href="/_polymer/sky-ajax-table1.html">
    <link rel="import" href="/_polymer/sky-html.html">
    <link rel="import" href="/_polymer/sky-breadcrumbs.html">
    <link rel="import" href="/_polymer/sky-form-text.html">
    <link rel="import" href="/_polymer/sky-ajax-thumbnails.html">
    <link rel="import" href="/_polymer/sky-links.html">

    <link rel="import" href="/_polymer/bower_components/google-chart/google-chart.html">


    <link rel="stylesheet" href="/webapp/css/select2.css">
    <link rel="stylesheet" href="/webapp/css/select2-bootstrap.css">

    <link rel="stylesheet" href="/webapp/js/animate/animate.css">

    <!-- jstree -->
    <link rel="stylesheet" href="/static/themes/jstree/style.min.css" />
    <script src="/static/js/jstree/jstree.min.js"></script>

    <!-- images in bootstrap menus
    <style>
        .dropdown-menu a.icon {
		  position: relative;
		  padding-left: 40px;
		}
		.dropdown-menu a.icon .glyphicon {
		  top: 6px;
		  left: 10px;
		  font-size: 18px;
		}
    </style>-->

    <!-- mousetrap -->
    <script src="/static/js/mousetrap/mousetrap.min.js"></script>

    <script type="text/javascript">
        Mousetrap.bind('command+s', function(e) {
            alert("keyboard shortcut for save not yet implemented");
        });

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

    <script type='text/javascript'>
        \$(window).load(function(){
          \$("input[type=date]").each(function() {
            if  (this.type != 'date' ) \$(this).datepicker({
            	  dateFormat: "yy-mm-dd"
            });
          });
        });
    </script>

    <script type="text/javascript" src="/webapp/js/select2.js"></script>
	$select2JsHead(model.formfields)$
    <script type="text/javascript" src="/webapp/js/moment.js"></script>
    $endOfHeaderJS(model.formfields)$

  </head>

  <body role="document" fullbreed unresolved>

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
                <li><a href='javascript:document.cookie="mainpage=;path=/";window.location.reload(true)'>Request/Response View</a></li>
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

      $mainContentSpa(user, converter, messages, model)$

      $if(converter.debug && user.principal)$
        $debug(messages, model)$
      $endif$

    </div>

    <footer class="footer">
      <div class="container4footer">
        <p class="text-muted"><div id="output"></div><div style="text-align:right;"><small>resource processed in $model.headers.("X-Duration")$ ms</small></div></p>
      </div>
    </footer>

    <script>
        var output = document.getElementById("output");

        function printMe(msg) {
      		output.innerHTML = msg;
        }

        if (typeof(EventSource) !== "undefined") {
            // disabled for now
        	//var source = new EventSource("/SSE");
            //source.onmessage = function(event) {
            //    var msgArray = event.data.split('\n');
            //    var timeout = Math.round(5000 / msgArray.length);
        	//	for (var i = 0; i < msgArray.length; i++) {
        	//		console.log(msgArray[i] + " " + (i * timeout));
        	//		setTimeout(printMe, i * timeout, msgArray[i]);
        	//	}
            //};
        } else {
            output.innerHTML = "<!-- SSE not supported -->";
        }
    </script>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/webapp/js/bootstrap.min.js"></script>
    $notifications(converter.notifications)$
    $select2JsEndOfBody(model.formfields)$

    $if(model.target.treeForm)$
    <script>
	  \$(function () {
	    // 6 create an instance when the DOM is ready
	    \$('#jstree').jstree();
	    // 7 bind to events triggered on the tree
	    \$('#jstree').on("changed.jstree", function (e, data) {
	      console.log(data.selected);
	    });
	    // 8 interact with the tree - either way is OK
	    \$('button').on('click', function () {
	      \$('#jstree').jstree(true).select_node('child_node_1');
	      \$('#jstree').jstree('select_node', 'child_node_1');
	      \$.jstree.reference('#jstree').select_node('child_node_1');
	    });
	  });
    </script>
    $endif$
  </body>
</html>

>>