index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en-gb" dir="ltr" style="height:100%; min-height:100%;">

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>skysail designer application</title>
        <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon">
        <link rel="apple-touch-icon-precomposed" href="images/apple-touch-icon.png">
        
        <link rel="stylesheet" type="text/css" href="/static/vitmalina/w2ui/1.4.3/w2ui-1.4.3.min.css" />
        <!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>-->
        $jquery()$
        <script type="text/javascript" src="/static/vitmalina/w2ui/1.4.3/w2ui-1.4.3.min.js"></script>
        
        $trix()$
        
        <link rel="stylesheet" href="/webapp/css/bootstrap.glyphicons.css" />
    </head>

    <body>
    
      <div>
      $mainContent(user, converter, messages, model)$

      $if(converter.debug && user.principal)$
        $debug(messages, model)$
      $endif$
      </div>
      
    </body>
</html>
>>
