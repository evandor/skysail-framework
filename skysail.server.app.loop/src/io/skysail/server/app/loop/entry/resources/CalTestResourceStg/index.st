index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel='stylesheet' href='//cdnjs.cloudflare.com/ajax/libs/fullcalendar/2.6.1/fullcalendar.min.css' />
    <script src='http://code.jquery.com/jquery-2.2.1.min.js'></script>
    <script src='/static/js/moment/2.7.0/moment.js'></script>
    <script src='//cdnjs.cloudflare.com/ajax/libs/fullcalendar/2.6.1/fullcalendar.js'></script>
</head>
<body>

    <div id='calendar'>yyy</div>
       
    <script type="text/javascript">
        \$(document).ready(function() {
            \$('#calendar').fullCalendar({
                header: {
                    left:   'title',
                    center: '',
                    right:  'today prev,next'
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