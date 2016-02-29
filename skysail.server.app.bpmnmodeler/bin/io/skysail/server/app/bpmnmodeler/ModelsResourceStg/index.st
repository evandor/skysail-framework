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
    
    <link rel="stylesheet" href="/static/bpmn.io/bpmn-js/0.13.1/css/diagram-js.css" />
    <link rel="stylesheet" href="/static/bpmn.io/bpmn-js/0.13.1/vendor/bpmn-font/css/bpmn-embedded.css" />
    <link rel="stylesheet" href="/static/bpmn.io/bpmn-js/0.13.1/css/app.css" />
    

    $pace()$
    $jquery()$
    $bootstrap()$

    $jstree()$
    $cookieConsent()$

    <style>
      .form-group.required .control-label:before{
        color: red;
        content: "*";
        position: absolute;
        margin-left: -15px;
      }
    </style>

  </head>

  <body role="document">

    <!-- Fixed navbar (upper navigation, menu, search, current user)-->
    <nav class="navbar navbar-inverse navbar-fixed-top">
      $navbar(user, messages, converter, model)$
    </nav>

    <div class="container-fluid" style="padding-top:55px;">

        <div class="content" id="js-drop-zone" style="height:600px;">
      
          <div class="message intro">
            <div class="note">
              Drop BPMN diagram from your desktop or <a id="js-create-diagram" href>create a new diagram</a> to get started.
            </div>
          </div>
      
          <div class="message error">
            <div class="note">
              <p>Ooops, we could not display the BPMN 2.0 diagram.</p>
      
              <div class="details">
                <span>cause of the problem</span>
                <pre></pre>
              </div>
            </div>
          </div>
      
          <div class="canvas" id="js-canvas"></div>
        </div>
      
        <ul class="buttons">
          <li>
            download
          </li>
          <li>
            <a id="js-download-diagram" href title="download BPMN diagram">
              BPMN diagram
            </a>
          </li>
          <li>
            <a id="js-download-svg" href title="download as SVG image">
              SVG image
            </a>
          </li>
        </ul>
      
        <script src="/static/bpmn.io/bpmn-js/0.13.1/index.js"></script>

    </div>


    <script src="/webapp/js/bootstrap.min.js"></script>

  </body>
</html>

>>