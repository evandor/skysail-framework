indexSpa(user, messages, converter, model) ::= <<
<!doctype html>
<html lang="">

<head>
  <meta charset="utf-8">
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="generator" content="Polymer Starter Kit" />
  <title>Polymer Starter Kit</title>
  <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->

  <!-- Chrome for Android theme color -->
  <meta name="theme-color" content="#303F9F">

  <!-- Web Application Manifest -->
  <link rel="manifest" href="manifest.json">

  <!-- Tile color for Win8 -->
  <meta name="msapplication-TileColor" content="#3372DF">

  <!-- Add to homescreen for Chrome on Android -->
  <meta name="mobile-web-app-capable" content="yes">
  <meta name="application-name" content="Polymer Starter Kit">
  <link rel="icon" sizes="192x192" href="images/touch/chrome-touch-icon-192x192.png">

  <!-- Add to homescreen for Safari on iOS -->
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="apple-mobile-web-app-title" content="Polymer Starter Kit">
  <link rel="apple-touch-icon" href="images/touch/apple-touch-icon.png">

  <!-- Tile icon for Win8 (144x144) -->
  <meta name="msapplication-TileImage" content="images/touch/ms-touch-icon-144x144-precomposed.png">

  <!-- build:css styles/main.css 
  <link rel="stylesheet" href="styles/main.css">-->
  
  <style>
  body {
  background: #fafafa;
  font-family: 'Roboto', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  color: #333;
  }
  </style>
  
  <!-- endbuild-->

  <!-- build:js bower_components/webcomponentsjs/webcomponents-lite.min.js -->
  <script src="/_polymer/bower_components/webcomponentsjs/webcomponents-lite.min.js"></script>
  <!-- endbuild -->

  <!-- will be replaced with elements/elements.vulcanized.html -->
  <!-- Iron elements -->
  <link rel="import" href="/_polymer/bower_components/iron-flex-layout/classes/iron-flex-layout.html">
  <link rel="import" href="/_polymer/bower_components/iron-icons/iron-icons.html">
  <link rel="import" href="/_polymer/bower_components/iron-pages/iron-pages.html">
  <link rel="import" href="/_polymer/bower_components/iron-selector/iron-selector.html">
  
  <!-- Paper elements -->
  <link rel="import" href="/_polymer/bower_components/paper-drawer-panel/paper-drawer-panel.html">
  <link rel="import" href="/_polymer/bower_components/paper-header-panel/paper-header-panel.html">
  <link rel="import" href="/_polymer/bower_components/paper-icon-button/paper-icon-button.html">
  <link rel="import" href="/_polymer/bower_components/paper-item/paper-item.html">
  <link rel="import" href="/_polymer/bower_components/paper-material/paper-material.html">
  <link rel="import" href="/_polymer/bower_components/paper-menu/paper-menu.html">
  <link rel="import" href="/_polymer/bower_components/paper-styles/paper-styles-classes.html">
  <link rel="import" href="/_polymer/bower_components/paper-toast/paper-toast.html">
  <link rel="import" href="/_polymer/bower_components/paper-toolbar/paper-toolbar.html">
  
  <!-- Add your elements here
  <link rel="import" href="../styles/app-theme.html">-->
  <link rel="import" href="my-greeting/my-greeting.html">
  <link rel="import" href="my-list/my-list.html">
  
  <style is="custom-style">

  /*
   Polymer includes a shim for CSS Custom Properties that we can use for application theming.
   Below, you'll find the default palette for the Polymer Starter Kit layout. Feel free to play
   with changing the colors used or generate your own palette of colours at MaterialPalette.com.

   See https://www.polymer-project.org/1.0/docs/devguide/styling.html#xscope-styling-details
   for further information on custom CSS properties.
   */

  /* Application theme */

  :root {
    --dark-primary-color: #303F9F;
    --default-primary-color: #3F51B5;
    --light-primary-color: #C5CAE9;
    --text-primary-color: #ffffff; /*text/icons*/
    --accent-color: #FF4081;
    --primary-background-color: #c5cae9;
    --primary-text-color: #212121;
    --secondary-text-color: #727272;
    --disabled-text-color: #bdbdbd;
    --divider-color: #B6B6B6;

    /* Components */

    /* paper-drawer-panel */
    --drawer-menu-color: #ffffff;
    --drawer-border-color: 1px solid #ccc;
    --drawer-toolbar-border-color: 1px solid rgba(0, 0, 0, 0.22);

    /* paper-menu */
    --paper-menu-background-color: #fff;
    --menu-link-color: #111111;
  }

  /* General styles */

  #drawerToolbar {
    color: var(--secondary-text-color);
    background-color: var(--drawer-menu-color);
    border-bottom: var(--drawer-toolbar-border-color);
    height: 64px;
  }

  paper-material {
    border-radius: 2px;
    height: 100%;
    padding: 16px 0 16px 0;
    width: calc(98.66% - 16px);
    margin: 16px auto;
    background: white;
  }

  paper-menu iron-icon {
    margin-right: 33px;
    opacity: 0.54;
  }

  .paper-menu > .iron-selected {
    color: var(--default-primary-color);
  }

  paper-menu a {
    text-decoration: none;
    color: var(--menu-link-color);
    display: -ms-flexbox;
    display: -webkit-flex;
    display: flex;
    -ms-flex-direction: row;
    -webkit-flex-direction: row;
    flex-direction: row;
    -ms-flex-align: center;
    -webkit-align-items: center;
    align-items: center;
    font-family: 'Roboto', 'Noto', sans-serif;
    -webkit-font-smoothing: antialiased;
    text-rendering: optimizeLegibility;
    font-size: 14px;
    font-weight: 400;
    line-height: 24px;
    min-height: 48px;
    padding: 0 16px;
  }

  #mainToolbar .middle {
    margin-left: 48px;
  }

  #mainToolbar.has-shadow .middle {
    font-size: 20px;
    padding-bottom: 0;
    margin-left: 48px;
  }

  /* Height of the scroll area */
  .content {
    height: 900px;
  }

  /* Breakpoints */

  /* Small */
  @media (max-width: 600px) {

    paper-material {
      --menu-container-display: none;
      width: calc(97.33% - 32px);
      padding-left: 16px;
      padding-right: 16px;
    }

    .paper-font-display1 {
      font-size: 12px;
    }

    .app-name {
      font-size: 26px;
    }

    #drawer .paper-toolbar {
      margin-left: 16px;
    }

    #overlay {
      min-width: 360px;
    }

    .bg {
      background: white;
    }

  }

  /* Tablet+ */
  @media (min-width: 601px) {

    paper-material {
      width: calc(98% - 46px);
      margin-bottom: 32px;
      padding-left: 30px;
      padding-right: 30px;
    }

    #drawer.paper-drawer-panel > [drawer] {
      border-right: 1px solid rgba(0, 0, 0, 0.14);
    }

    iron-pages {
      padding: 48px 62px;
    }

  }

  /* Material Design Adaptive Breakpoints */
  /*
    Below you'll find CSS media queries based on the breakpoint guidance
    published by the Material Design team. You can choose to use, customise
    or remove these breakpoints based on your needs.

    http://www.google.com/design/spec/layout/adaptive-ui.html#adaptive-ui-breakpoints
   */

  /* mobile-small */
  @media all and (min-width: 0) and (max-width: 360px) and (orientation: portrait) { }
  /* mobile-large */
  @media all and (min-width: 361px) and (orientation: portrait) { }
  /* mobile-small-landscape */
  @media all and (min-width: 0) and (max-width: 480px) and (orientation: landscape) { }
  /* mobile-large-landscape */
  @media all and (min-width: 481px) and (orientation: landscape) { }
  /* tablet-small-landscape */
  @media all and (min-width: 600px) and (max-width: 960px) and (orientation: landscape) { }
  /* tablet-large-landscape */
  @media all and (min-width: 961px) and (orientation: landscape) { }
  /* tablet-small */
  @media all and (min-width: 600px) and (orientation: portrait) { }
  /* tablet-large */
  @media all and (min-width: 601px) and (max-width: 840px) and (orientation : portrait) { }
  /* desktop-x-small-landscape */
  @media all and (min-width: 0) and (max-width: 480px) and (orientation: landscape) { }
  /* desktop-x-small */
  @media all and (min-width: 0) and (max-width: 480px) and (max-aspect-ratio: 4/3) { }
  /* desktop-small-landscape */
  @media all and (min-width: 481px) and (max-width: 840px) and (orientation: landscape) { }
  /* desktop-small */
  @media all and (min-width: 481px) and (max-width: 840px) and (max-aspect-ratio: 4/3) { }
  /* desktop-medium-landscape */
  @media all and (min-width: 841px) and (max-width: 1280px) and (orientation: landscape) { }
  /* desktop-medium */
  @media all and (min-width: 841px) and (max-width: 1280px) and (max-aspect-ratio: 4/3) { }
  /* desktop-large */
  @media all and (min-width: 1281px) and (max-width: 1600px) { }
  /* desktop-xlarge */
  @media all and (min-width: 1601px) and (max-width: 1920px) { }
</style>
  
  <!-- Configure your routes here 
  <link rel="import" href="routing.html">-->

<script src="/_polymer/bower_components/page/page.js"></script>
<script>
  window.addEventListener('WebComponentsReady', function() {

    // We use Page.js for routing. This is a Micro
    // client-side router inspired by the Express router
    // More info: https://visionmedia.github.io/page.js/
    page('/', function () {
      app.route = 'home';
    });

    page('/users', function () {
      app.route = 'users';
    });

    page('/users/:name', function (data) {
      app.route = 'user-info';
      app.params = data.params;
    });

    page('/contact', function () {
      app.route = 'contact';
    });

    // add #! before urls
    page({
      hashbang: true
    });

  });
</script>



  <!-- endreplace-->
</head>

<body unresolved class="fullbleed layout vertical">
  <span id="browser-sync-binding"></span>
  <template is="dom-bind" id="app">

    <paper-drawer-panel id="paperDrawerPanel">
      <div drawer>

        <!-- Drawer Toolbar -->
        <paper-toolbar id="drawerToolbar">
          <span class="paper-font-title">Menu</span>
        </paper-toolbar>

        <!-- Drawer Content -->
          <paper-menu class="list" attr-for-selected="data-route" selected="{{route}}" on-iron-select="onMenuSelect">
              <a data-route="home" href="/">
                <iron-icon icon="home"></iron-icon>
                <span>Home</span>
              </a>

              <a data-route="users" href="/users">
                <iron-icon icon="info"></iron-icon>
                <span>Users</span>
              </a>

              <a data-route="contact" href="/contact">
                <iron-icon icon="mail"></iron-icon>
                <span>Contact</span>
              </a>
          </paper-menu>
      </div>
      <paper-header-panel main mode="waterfall-tall">

        <!-- Main Toolbar -->
        <paper-toolbar id="mainToolbar">
          <paper-icon-button id="paperToggle" icon="menu" paper-drawer-toggle></paper-icon-button>
          <span class="flex"></span>

          <!-- Toolbar icons -->
          <paper-icon-button icon="refresh"></paper-icon-button>
          <paper-icon-button icon="search"></paper-icon-button>

          <!-- Application name -->
          <div class="middle paper-font-display2 app-name">Polymer Starter Kit</div>

          <!-- Application sub title -->
          <div class="bottom title"></div>

        </paper-toolbar>

        <!-- Main Content -->
        <div class="content">
          <iron-pages attr-for-selected="data-route" selected="{{route}}">

            <section data-route="home">
              <paper-material elevation="1">
                <my-greeting></my-greeting>

                <p class="paper-font-subhead">You now have:</p>
                <my-list></my-list>

                <p class="paper-font-body2">Looking for more Web App layouts? Check out our <a href="https://github.com/PolymerElements/app-layout-templates">layouts</a> collection. You can also <a href="http://polymerelements.github.io/app-layout-templates/">preview</a> them live.</p>
              </paper-material>
              <paper-material elevation="1">
                <p class="paper-font-body2">This is another card.</p>
              </paper-material>

              <paper-material elevation="1" class="paper-font-body2">
                <h1 id="license">License</h1>
                <p>Everything in this repo is BSD style license unless otherwise specified.</p>
                <p>Copyright (c) 2015 The Polymer Authors. All rights reserved.</p>
                <p>Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:</p>
                <ul>
                <li>Redistributions of source code must retain the above copyright
                notice, this list of conditions and the following disclaimer.</li>
                <li>Redistributions in binary form must reproduce the above
                copyright notice, this list of conditions and the following disclaimer
                in the documentation and/or other materials provided with the
                distribution.</li>
                <li>Neither the name of Google Inc. nor the names of its
                contributors may be used to endorse or promote products derived from
                this software without specific prior written permission.</li>
                </ul>
                <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS &quot;AS IS&quot; AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.</p>
              </paper-material>

            </section>

            <section data-route="users">
              <paper-material elevation="1">
                <h2 class="paper-font-display2">Users</h2>
                <p>This is the users section</p>
                <a href="/users/Rob">Rob</a>
              </paper-material>
            </section>

            <section data-route="user-info">
              <paper-material elevation="1">
                <h2 class="paper-font-display2">
                User:<span>{{params.name}}</span>
                </h2>
                <div>This is <span>{{params.name}}</span>'s section</div>
              </paper-material>
            </section>

            <section data-route="contact">
              <paper-material elevation="1">
                <h2 class="paper-font-display2">Contact</h2>
                <p>This is the contact section</p>
              </paper-material>
            </section>

          </iron-pages>
        </div>
      </paper-header-panel>
    </paper-drawer-panel>

  </template>

  <!-- build:js scripts/app.js -->
  <script>
  
  (function(document) {
	  'use strict';

	  // Grab a reference to our auto-binding template
	  // and give it some initial binding values
	  // Learn more about auto-binding templates at http://goo.gl/Dx1u2g
	  var app = document.querySelector('#app');

	  app.displayInstalledToast = function() {
	    document.querySelector('#caching-complete').show();
	  };

	  // Listen for template bound event to know when bindings
	  // have resolved and content has been stamped to the page
	  app.addEventListener('dom-change', function() {
	    console.log('Our app is ready to rock!');
	  });

	  // See https://github.com/Polymer/polymer/issues/1381
	  window.addEventListener('WebComponentsReady', function() {
	    // imports are loaded and elements have been registered
	  });

	  // Close drawer after menu item is selected if drawerPanel is narrow
	  app.onMenuSelect = function() {
	    var drawerPanel = document.querySelector('#paperDrawerPanel');
	    if (drawerPanel.narrow) {
	      drawerPanel.closeDrawer();
	    }
	  };

	})(document);

  
  </script>
  <!-- endbuild-->
</body>

</html>
>>