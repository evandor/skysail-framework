index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>$messages.("productName").translated$</title>
    <link rel="shortcut icon" href="/webapp/favicon.ico" type="image/x-icon" >

    $pace()$
    $trix()$
    $jquery()$
     <script src="/webapp/js/jinplace/1.2.1/jinplace.js"></script>
    $bootstrap()$
    $polymer()$
    $select2(model.formfields)$
    $animate()$
    $jstree()$
    $mousetrap()$
    $datepicker()$
    $moment()$
    $cookieConsent()$
    $endOfHeaderJS(model.formfields)$

    <style>
.form-group.required .control-label:before {
	color: red;
	content: "*";
	position: absolute;
	margin-left: -15px;
}

.sidebar-nav li {
    line-height: 18px;
    margin-bottom: 3px;
}

.sidebar-nav .preview {
    color: #666;
    font-size: 13px;
    line-height: 30px;
}

.sidebar-nav li .lyrow {
    margin-bottom: 5px;
}
.lyrow {
    margin-bottom: 10px;
}
.box, .lyrow {
    position: relative;
}

.sidebar-nav .nav-header {
    color: #000;
    text-shadow: 0 1px 0 #ddd;
}
.sidebar-nav .nav-header {
    color: #579;
    cursor: pointer;
    font-size: 14px;
    font-weight: bold;
    text-transform: uppercase;
}

.sidebar-nav .preview input {
	background: #bbb none repeat scroll 0 0;
	color: #999;
	font-size: 10px;
	height: 20px;
	line-height: 20px;
	padding: 0 10px;
	position: relative;
	top: -1px;
	width: 130px;
}
.edit .drag {
    cursor: pointer;
}


.edit .htmlpage::after {
    background-color: #f5f5f5;
    border: 1px solid #dddddd;
    border-radius: 4px 0;
    color: #9da0a4;
    content: "Container";
    font-size: 12px;
    font-weight: bold;
    left: -1px;
    padding: 3px 7px;
    position: absolute;
    top: -1px;
}

.edit .htmlpage {
    border: 1px solid #dddddd;
    border-radius: 4px;
    margin-left: 0;
    margin-top: 10px;
    padding: 30px 15px 15px;
    position: relative;
    word-wrap: break-word;
}
</style>

  </head>

  <body role="document">

    <!-- Fixed navbar (upper navigation, menu, search, current user)-->
    <nav class="navbar navbar-inverse navbar-fixed-top">
      $navbar(user, messages, converter, model)$
    </nav>

    <div class="container-fluid" style="padding-top:55px;">

      $mainContent(user, converter, messages, model)$

      $if(converter.debug && user.principal)$
        $debug(messages, model)$
      $endif$


    </div>

    <footer class="footer">
      <div class="container4footer" style="border-top: 1px dotted #a0a0a0; margin-right: 18px;">
        <div class="row">
          <div class="col-md-8" id="output"></div>
          <div class="col-md-3"></div>
          <div class="col-md-1" style="margin-top:10px; text-align: right">$peityBars(converter.peitybars)$</div>
         </div>
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
    <script src="/webapp/js/bootstrap.min.js"></script>
    $notifications(converter.notifications)$

    $select2JsEndOfBody(model.formfields)$

    $peity()$
    <script type="text/javascript">
        \$(".bar").peity("bar");

        \$(function () {
        	  \$('[data-toggle="tooltip"]').tooltip()
        });

    </script>
    
    <script type="text/javascript">
    \$('.editable').jinplace();
    </script>

    <script>
    function openWindow(target,name) {
        var myWindow = window.open(target, name, "width=800, height=600, location=no, top=150, left=250, menubar=no, status=yes");
    }
    </script>

  </body>
</html>

>>