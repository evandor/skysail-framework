index(user, messages, source, converter, services, resource, fields, target) ::= <<
<!DOCTYPE html>
<html lang="en">
  
  $head()$


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