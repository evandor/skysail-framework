index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en-gb" dir="ltr" style="height:100%; min-height:100%;">

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>skysail designer application</title>
        <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon">
        <link rel="apple-touch-icon-precomposed" href="images/apple-touch-icon.png">
        
        <link rel="stylesheet" href="/static/yootheme/uikit/2.24.3/css/uikit.almost-flat.min.css" />
        <script src="http://code.jquery.com/jquery-2.2.0.min.js"></script>
        <script src="/static/yootheme/uikit/2.24.3/js/uikit.min.js"></script>
        
        <!-- <link rel="stylesheet" href="/static/yootheme/uikit/2.24.3/css/uikit.docs.min.css"> -->
        <!--        <script src="../vendor/jquery.js"></script>-->
        <script src="/static/yootheme/uikit/2.24.3/js/uikit.min.js"></script>
        <!-- <script src="../src/js/components/sticky.js"></script>-->

        <script src="/static/yootheme/uikit/2.24.3/js/components/pagination.min.js"></script>
        
        <link rel="stylesheet" href="/webapp/css/bootstrap.glyphicons.css" />
        
        $trix()$
        
        $polymer()$
        
        <style type="text/css">
           
        </style>
    </head>

    <body style="margin:0px; height:100%; min-height: 100%;">
    
        <nav class="uk-navbar">
            <a href="/" class="uk-navbar-brand">$model.resource.application.name$</a>

            <!-- <ul class="uk-navbar-nav">
                <li data-uk-dropdown="" class="uk-parent uk-active">
                    <a href=""><i class="uk-icon-home"></i> Level1</a>
                    <div class="uk-dropdown uk-dropdown-navbar">
                        <ul class="uk-nav uk-nav-navbar">
                            <li><a href="#">Item</a></li>
                            <li><a href="#">Another item</a></li>
                            <li class="uk-nav-header">Header</li>
                            <li><a href="#">Item</a></li>
                            <li><a href="#">Another item</a></li>
                            <li class="uk-nav-divider"></li>
                            <li><a href="#">Separated item</a></li>
                        </ul>
                    </div>
                </li>
            </ul> -->

            <div class="uk-navbar-content uk-hidden-small">
                  <form class="uk-form uk-margin-remove uk-display-inline-block">
                      <input type="text" placeholder="Search" class="uk-form-small">
                      <button class="uk-button uk-button-primary uk-button-small">&gt;&gt;</button>
                  </form>
              </div>

              <!-- <div class="uk-navbar-content uk-navbar-flip  uk-hidden-small">
                  <div class="uk-button-group">
                      <a href="#" class="uk-button uk-button-danger">Link</a>
                      <button class="uk-button uk-button-danger">Button</button>
                  </div>
            </div> -->

        </nav>
    
        <div class="uk-container uk-container-center" style="height:100%; min-height: 100%;"> <!-- uk-margin-top uk-margin-large-bottom -->
            <div class="uk-grid" style="height:100%; min-height: 100%;"> <!-- data-uk-grid-margin -->
                <div class="uk-width-1-1">
                    $mainContent(user, converter, messages, model)$
                </div>
            </div>
            
            $if(converter.debug && user.principal)$
            <div class="uk-grid">
                <div class="uk-width-1-1">
                    $debug(messages, model)$
                </div>
            </div>
            $endif$
      
        </div>
        
    </body>
</html>
>>
