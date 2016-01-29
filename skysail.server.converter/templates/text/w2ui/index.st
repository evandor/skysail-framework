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
    </head>

    <body>
    
      <div id="myToolbar"></div>
      <div id="grid" style="width: 100%; height: 250px;"></div>
    
      <div>
      $mainContent(user, converter, messages, model)$

      $if(converter.debug && user.principal)$
        $debug(messages, model)$
      $endif$
      </div>
      
    </body>
    <script>
    \$(function () {
        \$('#myToolbar').w2toolbar({
            name : 'myToolbar',
            items: [
                { type: 'check',  id: 'item1', caption: 'Check', img: 'icon-add', checked: true },
                { type: 'break' },
                { type: 'menu',   id: 'item2', caption: 'Drop Down', img: 'icon-folder', 
                    items: [
                        { text: 'Item 1', img: 'icon-page' }, 
                        { text: 'Item 2', img: 'icon-page' }, 
                        { text: 'Item 3', img: 'icon-page' }
                    ]
                },
                { type: 'break' },
                { type: 'radio',  id: 'item3',  group: '1', caption: 'Radio 1', img: 'icon-page' },
                { type: 'radio',  id: 'item4',  group: '1', caption: 'Radio 2', img: 'icon-page' },
                { type: 'spacer' },
                { type: 'button',  id: 'item5',  caption: 'Item 5', img: 'icon-save' }
            ]
        });
    });    
    
\$(function () {
    \$('#grid').w2grid({
        name: 'grid',
        header: 'List of Names',
        columns: [
            { field: 'fname', caption: 'First Name', size: '30%' },
            { field: 'lname', caption: 'Last Name', size: '30%' },
            { field: 'email', caption: 'Email', size: '40%' },
            { field: 'sdate', caption: 'Start Date', size: '120px' }
        ],
        records: [
            { recid: 1, fname: "Peter", lname: "Jeremia", email: 'peter@mail.com', sdate: '2/1/2010' },
            { recid: 2, fname: "Bruce", lname: "Wilkerson", email: 'bruce@mail.com', sdate: '6/1/2010' },
            { recid: 3, fname: "John", lname: "McAlister", email: 'john@mail.com', sdate: '1/16/2010' },
            { recid: 4, fname: "Ravi", lname: "Zacharies", email: 'ravi@mail.com', sdate: '3/13/2007' },
            { recid: 5, fname: "William", lname: "Dembski", email: 'will@mail.com', sdate: '9/30/2011' },
            { recid: 6, fname: "David", lname: "Peterson", email: 'david@mail.com', sdate: '4/5/2010' }
        ]
    });
});
</script>
</html>
>>
