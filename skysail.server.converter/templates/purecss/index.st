index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en-gb" dir="ltr" style="height:100%; min-height:100%;">

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>skysail</title>
        <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon">
        <link rel="apple-touch-icon-precomposed" href="images/apple-touch-icon.png">
        <link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.6.0/pure-min.css">
        <link rel="stylesheet" href="/static/yahoo/purecss/0.6.0/pure.custom.css">
    </head>

    <body>
    
    $navbar(user, messages, converter, model)$
        
    <div class="pure-g">
      <div class="pure-u-1-1">
      $mainContent(user, converter, messages, model)$

      $if(converter.debug && user.principal)$
        $debug(messages, model)$
      $endif$
      </div>
    </div>
      
    </body>
</html>
>>
