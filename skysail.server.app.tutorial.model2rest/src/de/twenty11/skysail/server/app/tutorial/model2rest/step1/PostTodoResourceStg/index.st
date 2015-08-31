index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
  $head()$
  </head>

  <body role="document" style="background-color: #eeeeee;">
    <div class="container-fluid">
$mainContent(user, converter, messages, model)$
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/webapp/js/bootstrap.min.js"></script>

    $select2JsEndOfBody(fields)$

  </body>
</html>
>>