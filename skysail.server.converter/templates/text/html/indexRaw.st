indexRaw(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>RAW Theme</title>

    <link href="/webapp/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webapp/css/stickyfooter.css" rel="stylesheet">
    <link href="/webapp/css/bootstrap-theme.min.css" rel="stylesheet">

    <!-- polymer stuff -->
    <script src="/_polymer/bower_components/webcomponentsjs/webcomponents-lite.min.js"></script>
    <link rel="import" href="/_polymer/proto-element.html">
    <link rel="import" href="/_polymer/sky-app-nav-accordion.html">
    <link rel="import" href="/_polymer/sky-ajax-get-raw.html">
    <link rel="import" href="/_polymer/sky-ajax-table1-raw.html">
    <link rel="import" href="/_polymer/sky-html-raw.html">
    <link rel="import" href="/_polymer/sky-breadcrumbs.html">
    <link rel="import" href="/_polymer/sky-form-text.html">
    <link rel="import" href="/_polymer/sky-ajax-thumbnails.html">
    <link rel="import" href="/_polymer/sky-links.html">

    <link rel="import" href="/_polymer/bower_components/google-chart/google-chart.html">


    <link rel="stylesheet" href="/webapp/css/select2.css">
    <link rel="stylesheet" href="/webapp/css/select2-bootstrap.css">

    <link rel="stylesheet" href="/webapp/js/animate/animate.css">
  </head>

  <body role="document">

    <div class="container-fluid">
      $mainContentRaw(user, converter, messages, model)$
    </div>

  </body>
</html>

>>