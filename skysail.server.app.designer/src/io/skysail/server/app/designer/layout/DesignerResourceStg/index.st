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

    <script type="text/javascript" src="/webapp/js/bootstrap-plugins/multiselect/bootstrap-multiselect.js"></script>
    <link rel="stylesheet" type="text/css" href="/webapp/css/jointjs/joint.css" />
    <script src="/webapp/js/jquery/2.0.3/jquery.min.js"></script>
    <script src="/webapp/js/lodash/lodash.min.js"></script>
    <script src="/webapp/js/backbone/backbone.min.js"></script>
    <script src="/webapp/js/jointjs/joint.js"></script>

    $pace()$
    $trix()$
    $bootstrap()$
    $jstree()$
    $mousetrap()$
    $datepicker()$
    $moment()$
    $cookieConsent()$

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
    
  </body>
</html>

>>