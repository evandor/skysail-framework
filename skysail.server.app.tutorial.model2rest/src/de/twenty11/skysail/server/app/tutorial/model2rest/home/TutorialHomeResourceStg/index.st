index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Welcome to skysail</title>

    <!-- Bootstrap Core CSS -->
    <link href="/website/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/website/css/grayscale.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/website/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Lora:400,700,400italic,700italic" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script>
		  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

		  ga('create', 'UA-58670225-1', 'auto');
		  ga('send', 'pageview');
    </script>

</head>

<body id="page-top" data-spy="scroll" data-target=".navbar-fixed-top" style="background-color: #fff;">

    <!-- Navigation -->
    <nav class="navbar navbar-custom navbar-fixed-top" role="navigation" style="background-color: #000;">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-main-collapse">
                    <i class="fa fa-bars"></i>
                </button>
                <a class="navbar-brand page-scroll" href="/">
                    <i class="fa fa-play-circle"></i>  <span class="light">skysail</span>
                </a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse navbar-right navbar-main-collapse">
                <ul class="nav navbar-nav">
                    <!-- Hidden li included to remove active class from about link when scrolled up past about section -->
                    <li class="hidden">
                        <a href="#page-top"></a>
                    </li>
                    <li>
                        <a class="page-scroll" href="/website#about">About</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="/website#learnmore">Tutorial</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="/website#contact">Contact</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

    <section id="learnmore" class="content-section text-center" style="padding-top: 104px;">
    <div class="container-fluid" style="color: #000; text-align: justify;">
$mainContent(user, resource, converter, messages, source, fields)$
    </div>
	</section>

    <!-- Footer -->
    <footer>
        <div class="container text-center">
            <p>Made with <span class="glyphicon glyphicon-heart" aria-hidden="true"></span> in Munich &copy; 2015</p>
        </div>
    </footer>

    <!-- jQuery -->
    <script src="/website/js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="/website/js/bootstrap.min.js"></script>

    <!-- Plugin JavaScript -->
    <script src="/website/js/jquery.easing.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="/website/js/grayscale.js"></script>

</body>

</html>
>>