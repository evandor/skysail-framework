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
    $jquery()$
    $bootstrap()$
    $select2(model.formfields)$
    $animate()$
    $jstree()$
    $mousetrap()$
    $datepicker()$
    $moment()$
    $cookieConsent()$
    $endOfHeaderJS(model.formfields)$

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
      <div class="container4footer" style="border-top: 1px dotted #a0a0a0;">
        <div id="output"></div>
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


  </body>
</html>

>>