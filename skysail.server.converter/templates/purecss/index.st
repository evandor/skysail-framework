index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en-gb" dir="ltr" style="height:100%; min-height:100%;">

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>skysail designer application</title>
        <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon">
        <link rel="apple-touch-icon-precomposed" href="images/apple-touch-icon.png">
        <link rel="stylesheet" href="/static/yahoo/purecss/0.6.0/pure.css"></script>
    </head>

    <body>
    
    <div class="pure-menu pure-menu-horizontal">
      <a href="#" class="pure-menu-heading pure-menu-link">BRAND</a>
      <ul class="pure-menu-list">
        <li class="pure-menu-item"><a href="#" class="pure-menu-link">News</a></li>
        <li class="pure-menu-item"><a href="#" class="pure-menu-link">Sports</a></li>
        <li class="pure-menu-item"><a href="#" class="pure-menu-link">Finance</a></li>
      </ul>
    </div>
    
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
