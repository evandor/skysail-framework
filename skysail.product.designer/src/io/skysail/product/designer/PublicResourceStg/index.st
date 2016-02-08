index(user, messages, converter, model) ::= <<
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>$messages.("productName").translated$</title>
<link rel="shortcut icon" href="/webapp/favicon.ico" type="image/x-icon">
<!-- <link rel="stylesheet" href="../assets/css/custom.min.css"> -->

$pace()$ $trix()$ $jquery()$

<!--  bootstrap -->
<link href="https://bootswatch.com/paper/bootstrap.min.css" rel="stylesheet">

$animate()$ $moment()$ $cookieConsent()$

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

<style type="text/css">
.skysail_feature:hover .skysail_feature::after {
	border: 2px solid #ffffff !important;
}

.skysail_feature:hover i {
	top: -20px;
}

.skysail_feature:hover i, .skysail_feature:hover h5 {
	cursor: pointer;
}

.skysail_feature i {
	display: block;
	font-size: 60px;
	line-height: 60px;
	position: relative;
	transition: all 0.4s ease 0.1s;
	z-index: 3;
}

.skysail_feature h5 {
	font-size: 16px;
	font-weight: 800;
	letter-spacing: -1px;
	line-height: 19px;
	margin: 0 0 30px;
	padding-top: 34px;
	text-transform: uppercase;
	transition: all 150ms linear 0s;
}

.skysail_feature p {
	color: #222222;
	font-size: 14px;
	line-height: 24px;
}

.skysail_feature a {
	margin-top: 10px;
	padding: 8px;
	text-decoration: none;
}

.skysail_feature a:hover {
	text-decoration: none;
}

.footer_b {
    background: #222222 none repeat scroll 0 0;
    border-top: 1px solid #555555;
}

.footer_top {
    background: #333333 none repeat scroll 0 0;
    border-bottom: 1px solid #0f0f0f;
    color: #aaaaaa;
    overflow: hidden;
    padding: 30px 0 50px;
}
</style>

</head>

<body role="document">

  <div class="navbar navbar-default navbar-fixed-top">
    <div class="container">
      <div class="navbar-header">
        <a href="../" class="navbar-brand">Skysail.io </a>
        <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
          <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
        </button>
      </div>
      <div class="navbar-collapse collapse" id="navbar-main">
        <ul class="nav navbar-nav">
          <!-- <li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" href="#" id="themes">Themes <span class="caret"></span></a>
              <ul class="dropdown-menu" aria-labelledby="themes">
                <li><a href="../default/">Default</a></li>
                <li class="divider"></li>
                <li><a href="../cerulean/">Cerulean</a></li>
              </ul>
            </li>-->
          <li><a href="https://github.com/evandor/skysail-framework" target="_blank">Github</a></li>
          <li><a href="http://skysailserver.blogspot.de/" target="_blank">Blog</a></li>
          <li><a href="http://skysail.io/" target="_blank">Landing Page</a></li>
          <!-- <li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" href="#" id="download">Paper <span class="caret"></span></a>
              <ul class="dropdown-menu" aria-labelledby="download">
                <li><a href="http://jsfiddle.net/bootswatch/ndax7sh7/">Open Sandbox</a></li>
                <li class="divider"></li>
                <li><a href="./bootstrap.min.css">bootstrap.min.css</a></li>
                <li><a href="./bootstrap.css">bootstrap.css</a></li>
              </ul>
            </li>-->
        </ul>
        <ul class="nav navbar-nav navbar-right">
          <li><a href="_login">Login</a></li>
        </ul>
      </div>
    </div>
  </div>

  <div class="container">
    <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
      <ol class="carousel-indicators">
        <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
        <li data-target="#carousel-example-generic" data-slide-to="1"></li>
        <li data-target="#carousel-example-generic" data-slide-to="2"></li>
      </ol>
      <div class="carousel-inner" role="listbox">
        <div class="item active">
          <img src="/static/img/define.jpg" alt="define">
          <div class="carousel-caption">
            <h1>Define...</h1>
            <h4>...a new application with entities, relations and attributes</h4>
          </div>
        </div>
        <div class="item">
          <img src="/static/img/create.jpg" alt="create">
          <div class="carousel-caption">
            <h1>Create...</h1>
            <h4>...a running prototype with a single click</h4>
          </div>
        </div>
        <div class="item">
          <img src="/static/img/iterate.jpg" alt="iterate">
          <div class="carousel-caption">
            <h1>Iterate...</h1>
            <h4>...and improve your prototype by actually using it</h4>
          </div>
        </div>
      </div>
      <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev"> <span
        class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span> <span class="sr-only">Previous</span>
      </a> <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next"> <span
        class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span> <span class="sr-only">Next</span>
      </a>
    </div>
  </div>

  <div class="container">
    <div class="row">
      <div class="col-md-12">&nbsp;</div>
    </div>
  </div>
  
  <div class="container">
    <div class="jumbotron">
      <h1 style="text-align: center;">Rapid Prototyping with skysail</h1>
      <p class="lead" style="text-align: center;">
       You want to create a <b><i>real application</i></b> to validate your new idea?<br>
       You think <b>visual mockups</b> are not enough?<br>
       You want to actually <b><i>feel</i></b> how your new application is going to behave?</p>
      <p style="text-align: center;">
        <a class="btn btn-lg btn-success" href="#" role="button">Stay tuned</a>
      </p>
    </div>
  </div>

  <section id="service">
    <div class="container">
      <div class="row">
        <div class="col-md-12 col-sm-12 col-xs-12">
          <div class="feature_header text-center">
            <h3 class="feature_title">
              Download and follow three simple steps:
            </h3>
            <h4 class="feature_sub" contenteditable="true">You can create a new application easily - including various user interfaces, persistence and
              validation.</h4>
            <div class="divider"></div>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="main_feature text-center">
          <div class="col-md-4 col-xs-12 col-sm-6">
            <div class="skysail_feature">
              <i class="fa fa-lightbulb-o"></i>
              <h5>Define</h5>
              <p>You can not ignore mobile devices anymore and with this theme all your visitors will be very pleased how they see your
                website.</p>
              <button class="btn btn-main">Read More</button>
            </div>
          </div>
          <div class="col-md-4 col-xs-12 col-sm-6">
            <div class="skysail_feature">
              <i class="fa fa-pencil"></i>
              <h5>Create</h5>
              <p>This theme integrates with WordPress in the most awesome way! Functionality is separated from style through uncreadble
                useful for user.</p>
              <button class="btn btn-main">Read More</button>
            </div>
          </div>
          <div class="col-md-4 col-xs-12 col-sm-6">
            <div class="skysail_feature">
              <i class="fa fa-cog"></i>
              <h5>Iterate</h5>
              <p>Full Time support. Very much helpful and possesive at the same time. With all this in mind you won’t be outdated
                anytime soon. Really!!</p>
              <button class="btn btn-main">Read More</button>
            </div>
          </div>
        </div>
      </div>
      <!-- Row End -->
    </div>
    <!-- Container End -->
  </section>

  <section id="footer">
    <div class="footer_top">
      <div class="container">
        <div class="row">
          <div class="col-md-4 col-sm-6 col-xs-12">
            <h3 class="menu_head">Main Menu</h3>
            <div class="footer_menu">
              <ul>
                <li><a href="#about">Home</a></li>
              </ul>
            </div>
          </div>
          <div class="col-md-4 col-sm-6 col-xs-12">
            <h3 class="menu_head">Useful Links</h3>
            <div class="footer_menu">
              <ul>
                <li><a href="#">Terms of use</a></li>
              </ul>
            </div>
          </div>
          <div class="col-md-4 col-sm-6 col-xs-12">
            <h3 class="menu_head">Contact</h3>
            <div class="footer_menu_contact">
              <ul>
                <li><i class="fa fa-home"></i> <span> Adresse </span></li>
                <li><i class="fa fa-globe"></i> <span> Phone</span></li>
                <li><i class="fa fa-phone"></i> <span> info@skysail.io</span></li>
                <li><i class="fa fa-map-marker"></i> <span> www.skysail.io</span></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="footer_b">
      <div class="container">
        <div class="row">
          <div class="col-md-6 col-sm-6 col-xs-12">
            <div class="footer_bottom">
              <p class="text-block">
                &copy; <span>Skysail</span>
              </p>
            </div>
          </div>
          <div class="col-md-6 col-sm-6 col-xs-12">
            <div class="footer_mid pull-right">
              <ul class="social-contact list-inline">
                <li><a href="#"><i class="fa fa-facebook"></i></a></li>
                <li><a href="#"><i class="fa fa-twitter"></i></a></li>
                <li><a href="#"><i class="fa fa-rss"></i></a></li>
                <li><a href="#"><i class="fa fa-google-plus"></i> </a></li>
                <li><a href="#"> <i class="fa fa-linkedin"></i></a></li>
                <li><a href="#"> <i class="fa fa-pinterest"></i></a></li>
              </ul>
            </div>
          </div>

        </div>
      </div>
    </div>
  </section>
  
  <script src="/webapp/js/bootstrap.min.js"></script>
</body>
</html>

>>
