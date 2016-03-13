index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel='stylesheet' href='//cdnjs.cloudflare.com/ajax/libs/fullcalendar/2.6.1/fullcalendar.min.css' />
    <script src='http://code.jquery.com/jquery-2.2.1.min.js'></script>
    <script src='/static/js/moment/2.7.0/moment.js'></script>
    <script src='//cdnjs.cloudflare.com/ajax/libs/fullcalendar/2.6.1/fullcalendar.js'></script>

    <title>$messages.("productName").translated$</title>
    <link rel="shortcut icon" href="/webapp/favicon.ico" type="image/x-icon" >

    $pace()$in
    $bootstrap()$
    $cookieConsent()$
    
</head>

  <body role="document">

    <!-- Fixed navbar (upper navigation, menu, search, current user)-->
    <nav class="navbar navbar-inverse navbar-fixed-top">
      $navbar(user, messages, converter, model)$
    </nav>

    <div class="container-fluid" style="padding-top:55px;">
        <div id='calendar'></div>
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

    <script>
        var output = document.getElementById("output");

        function printMe(msg) {
            output.innerHTML = msg;
        }

        if (typeof(EventSource) !== "undefined") {
            // disabled for now
            //var source = new EventSource("/SSE");
            //source.onmessage = function(event) {
            //    var msgArray = event.data.split('\n');
            //    var timeout = Math.round(5000 / msgArray.length);
            //  for (var i = 0; i < msgArray.length; i++) {
            //      console.log(msgArray[i] + " " + (i * timeout));
            //      setTimeout(printMe, i * timeout, msgArray[i]);
            //  }
            //};
        } else {
            output.innerHTML = "<!-- SSE not supported -->";
        }
    </script>
    <script src="/webapp/js/bootstrap.min.js"></script>
    $notifications(converter.notifications)$

    $select2JsEndOfBody(model.formfields)$

    $peity()$
    <script type="text/javascript">
        \$(".bar").peity("bar");

        \$(function () {
              \$('[data-toggle="tooltip"]').tooltip()
        });
        
        \$(document).ready(function() {
            \$('#calendar').fullCalendar({
                header: {
                    left:   'title',
                    center: '',
                    right:  'today, month, agendaWeek, agendaDay, prev,next'
                },
                dayClick: function() {
                    alert('a day has been clicked!');
                }
            })
        });

    </script>
  </body>
</html>
>>