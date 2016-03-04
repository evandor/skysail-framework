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
    
    <style type="text/css">
      #paper {
        position: relative;
        border: 1px solid gray;
        display: inline-block;
        background: transparent;
        overflow: hidden;
      }

      #paper svg {
        background: transparent; 
      }
      
      #paper svg .link {
         z-index: 2;
      }
      
      .html-element {
         position: absolute;
         background: #3498DB;
         /* Make sure events are propagated to the JointJS element so, e.g. dragging works.*/
         pointer-events: none;
         -webkit-user-select: none;
         border-radius: 4px;
         border: 2px solid #2980B9;
         box-shadow: inset 0 0 5px black, 2px 2px 1px gray;
         padding: 5px;
         box-sizing: border-box;
         z-index: 2;
      }
      .html-element select,
      .html-element input,
      .html-element button {
         /* Enable interacting with inputs only. */
         pointer-events: auto;   
      }
.html-element button.delete {
   color: white;
   border: none;
   background-color: #C0392B;
   border-radius: 20px;
   width: 15px;
   height: 15px;
   line-height: 15px;
   text-align: middle;
   position: absolute;
   top: -15px;
   left: -15px;
   padding: 0;
   margin: 0;
   font-weight: bold;
   cursor: pointer;
}
.html-element button.delete:hover {
   width: 20px;
   height: 20px;
   line-height: 20px;
}
.html-element select {
   position: absolute;
   right: 2px;
   bottom: 28px;
}
.html-element input {
   position: absolute;
   bottom: 0;
   left: 0;
   right: 0;
   border: none;
   color: #333;
   padding: 5px;
   height: 16px;
}
.html-element label {
   color: #333;
   text-shadow: 1px 0 0 lightgray;
   font-weight: bold;
}
.html-element span {
   position: absolute;
   top: 2px;
   right: 9px;
   color: white;
   font-size: 10px;
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