index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Yahoo! Weather tutorial</title>
    
</head>
    <body>
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand">
                        <a href="#">
                            <a href="https://www.yahoo.com/?ilc=401" target="_blank"> <img class="brand" src="https://poweredby.yahoo.com/purple.png" width="134" height="29"/> </a>
                        </a>
                    </a>
                </div>

                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <form class="navbar-form navbar-right" role="search" action="javascript:getWeather()">
                        <div class="form-group">
                            <input id="city" type="text" class="form-control" placeholder="Enter City" required>
                        </div>
                        <button type="submit" class="btn btn-default">Get Forecast</button>
                    </form>
                </div>
            </div>
        </nav>

        <div class="container" hidden>
            <div class="jumbotron">
            </div>

            <div id="forecast" class="row">
            </div>
        </div>

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <link rel="stylesheet" href="css/tutorial.css">

        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.4.0/bootbox.min.js"></script>
            <script type="text/javascript">
    /* Sample function - not called */
    function getWeatherDemo() {
        \$.get('https://query.yahooapis.com/v1/public/yql?q=select * from weather.forecast ' +
              'where woeid in (select woeid from geo.places(1) where text="London")&format=json', function (data) {
            console.log(data);
            alert("The temperatute in London is " +
                data.query.results.channel.item.condition.temp +
                data.query.results.channel.units.temperature
            );
        });
    }

    function getWeather() {
        var location = \$('#city').val();

        \$.get('https://query.yahooapis.com/v1/public/yql?q=select * from weather.forecast where woeid in (select woeid from geo.places(1) where text="' + location + '")&format=json', function (data) {
            /* Check that a place was found (we'll just grab the first) */
            if (data.query.results === null) {
                bootbox.alert("Location not found: " + location + "!");

            } else {
                \$('.jumbotron').html('<h2>' + data.query.results.channel.item.title + '</h2>' +
                    data.query.results.channel.item.description)
                \$('.container').show();
            }

        });
    }
    </script>

    </body>
</html>
>>