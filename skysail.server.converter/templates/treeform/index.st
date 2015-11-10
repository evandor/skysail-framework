index(user, messages, source, converter, services, resource, fields, target) ::= <<
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Skysail</title>

    <!-- jQuery -->
    <script type="text/javascript" src="/webapp/js/jquery-1.11.1.js"></script>

    <link href="/webapp/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webapp/css/bootstrap-theme.min.css" rel="stylesheet">

    <!-- <link rel="stylesheet" href="//ivaynberg.github.io/select2/select2-3.5.2/select2.css"> -->
    <link rel="stylesheet" href="/webapp/css/select2.css">
    <link rel="stylesheet" href="/webapp/css/select2-bootstrap.css">

    <!-- jstree -->
    <link rel="stylesheet" href="/static/themes/jstree/style.min.css" />
    <script src="/static/js/jstree/jstree.min.js"></script>

    <script type="text/javascript" src="/webapp/js/select2.js"></script>
    $select2JsHead(fields)$
    <script type="text/javascript" src="/webapp/js/moment.js"></script>
    $endOfHeaderJS()$

    <style>
        #container { min-width:320px; margin:0px auto 0 auto; background:white; border-radius:0px; padding:0px; overflow:hidden; }
        #tree { float:left; min-width:319px; border-right:1px solid silver; overflow:auto; padding:0px 0; }
        #data { margin-left:320px; }
        #data textarea { margin:0; padding:0; height:100%; width:100%; border:0; background:white; display:block; line-height:18px; resize:none; }
        #data, #code { font: normal normal normal 12px/18px 'Consolas', monospace !important; }

        #tree .folder { background:url('./file_sprite.png') right bottom no-repeat; }
        #tree .file { background:url('./file_sprite.png') 0 0 no-repeat; }
        #tree .file-pdf { background-position: -32px 0 }
        #tree .file-as { background-position: -36px 0 }
        #tree .file-c { background-position: -72px -0px }
        #tree .file-iso { background-position: -108px -0px }
        #tree .file-htm, #tree .file-html, #tree .file-xml, #tree .file-xsl { background-position: -126px -0px }
        #tree .file-cf { background-position: -162px -0px }
        #tree .file-cpp { background-position: -216px -0px }
        #tree .file-cs { background-position: -236px -0px }
        #tree .file-sql { background-position: -272px -0px }
        #tree .file-xls, #tree .file-xlsx { background-position: -362px -0px }
        #tree .file-h { background-position: -488px -0px }
        #tree .file-crt, #tree .file-pem, #tree .file-cer { background-position: -452px -18px }
        #tree .file-php { background-position: -108px -18px }
        #tree .file-jpg, #tree .file-jpeg, #tree .file-png, #tree .file-gif, #tree .file-bmp { background-position: -126px -18px }
        #tree .file-ppt, #tree .file-pptx { background-position: -144px -18px }
        #tree .file-rb { background-position: -180px -18px }
        #tree .file-text, #tree .file-txt, #tree .file-md, #tree .file-log, #tree .file-htaccess { background-position: -254px -18px }
        #tree .file-doc, #tree .file-docx { background-position: -362px -18px }
        #tree .file-zip, #tree .file-gz, #tree .file-tar, #tree .file-rar { background-position: -416px -18px }
        #tree .file-js { background-position: -434px -18px }
        #tree .file-css { background-position: -144px -0px }
        #tree .file-fla { background-position: -398px -0px }
    </style>

  </head>

  <body role="document">

    <!-- Fixed navbar -->
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="/">Skysail</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            $dropdownFrontendsNav(services)$
            $dropdownApplicationsNav(services)$
            $dropdownAdminNav(services)$
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">View (advanced) <span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li class="divider"></li>
                <li class="dropdown-header">Nav header</li>
                <li><a href='javascript:document.cookie="template=debug;path=/";window.location.reload(true)'>Debug</a></li>
                <li><a href='javascript:document.cookie="template=edit;path=/";window.location.reload(true)'>Edit</a></li>
                <li><a href='javascript:document.cookie="inspect=on;path=/";window.location.reload(true)'>Inspect on</a></li>
                <li><a href='javascript:document.cookie="inspect=off;path=/";window.location.reload(true)'>Inspect off</a></li>
                <li><a href='javascript:document.cookie="template=simple;path=/";window.location.reload(true)'>Simple</a></li>
                <li><a href='javascript:document.cookie="mainpage=tests;path=/";window.location="/_largetests"'>Tests</a></li>
              </ul>
            </li>
            <li>
                <a href="/webapp/impress.html">About...</a>
            </li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li><a href="#">$user.principal$</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>

    <br><br><br>

    <div class="container-fluid">
      <div class="col-md-1">&nbsp;</div>
      <div class="col-md-10">
        <div class="row">
            $breadcrumb(resource)$
        </div>
        <div class="row">
            $menuNav(resource.linkheader)$
        </div>
        <div class="row">
            &nbsp;
        </div>
        <div class="row">
            $if(converter.edit)$
              <a href='/i18n/v1/messages/$resource.classname$.message/'>edit</a>
              $contentHeader(messages)$
            $else$
              $contentHeader(messages)$
            $endif$
        </div>
        <div class="row">

         <div id="jstree">
           <ul>
            <li>Application
              <ul>
                <li id="child_node_1">Routes
                <ul>
                	<li>1</li>
                	<li>2</li>
                </ul>
                </li>
                <li>Entities</li>
              </ul>
            </li>
          </ul>
        </div>
        <div id="data">
            <div class="content code" style="display:none;"><textarea id="code" readonly="readonly"></textarea></div>
            <div class="content folder" style="display:none;"></div>
            <div class="content image" style="display:none; position:relative;"><img src="" alt="" style="display:block; position:absolute; left:50%; top:50%; padding:0; max-height:90%; max-width:90%;" /></div>
            <div class="content default" style="text-align:center;">Select a file from the tree.</div>
        </div>

        </div>
        $if(converter.debug)$
            $debug(resource, source, target, messages)$
        $endif$
      </div>
      <div class="col-md-1">&nbsp;</div>
    </div>


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/webapp/js/bootstrap.min.js"></script>
    $notifications(converter.notifications)$
    $select2JsEndOfBody(fields)$

    <script>
    \$(function () {
		/*\$(window).resize(function () {
			var h = Math.max(\$(window).height() - 0, 420);
			\$('#container, #data, #tree, #data .content').height(h).filter('.default').css('lineHeight', h + 'px');
		}).resize();*/

		\$('#jstree')
			.jstree({
				'core' : {
					/*'data' : {
						'url' : '?operation=get_node',
						'data' : function (node) {
							return { 'id' : node.id };
						}
					},*/
					'check_callback' : function(o, n, p, i, m) {
						if(m && m.dnd && m.pos !== 'i') { return false; }
						if(o === "move_node" || o === "copy_node") {
							if(this.get_node(n).parent === this.get_node(p).id) { return false; }
						}
						return true;
					},
					'themes' : {
						'responsive' : false,
						'variant' : 'small',
						'stripes' : true
					}
				},
				'sort' : function(a, b) {
					return this.get_type(a) === this.get_type(b) ? (this.get_text(a) > this.get_text(b) ? 1 : -1) : (this.get_type(a) >= this.get_type(b) ? 1 : -1);
				},
				'contextmenu' : {
					'items' : function(node) {
						var tmp = \$.jstree.defaults.contextmenu.items();
						delete tmp.create.action;
						tmp.create.label = "New";
						tmp.create.submenu = {
							$submenu(fields)$
						};
						if(this.get_type(node) === "file") {
							delete tmp.create;
						}
						return tmp;
					}
				},
				'types' : {
					'default' : { 'icon' : 'folder' },
					'file' : { 'valid_children' : [], 'icon' : 'file' }
				},
				'unique' : {
					'duplicate' : function (name, counter) {
						return name + ' ' + counter;
					}
				},
				'plugins' : ['state','sort','types','contextmenu','unique']
			})
			.on('delete_node.jstree', function (e, data) {
				\$.get('?operation=delete_node', { 'id' : data.node.id })
					.fail(function () {
						data.instance.refresh();
					});
			})
			.on('create_node.jstree', function (e, data) {
				\$.get('?operation=create_node', { 'type' : data.node.type, 'id' : data.node.parent, 'text' : data.node.text })
					.done(function (d) {
						data.instance.set_id(data.node, d.id);
					})
					.fail(function () {
						data.instance.refresh();
					});
			})
			.on('rename_node.jstree', function (e, data) {
				\$.get('?operation=rename_node', { 'id' : data.node.id, 'text' : data.text })
					.done(function (d) {
						data.instance.set_id(data.node, d.id);
					})
					.fail(function () {
						data.instance.refresh();
					});
			})
			.on('move_node.jstree', function (e, data) {
				\$.get('?operation=move_node', { 'id' : data.node.id, 'parent' : data.parent })
					.done(function (d) {
						//data.instance.load_node(data.parent);
						data.instance.refresh();
					})
					.fail(function () {
						data.instance.refresh();
					});
			})
			.on('copy_node.jstree', function (e, data) {
				\$.get('?operation=copy_node', { 'id' : data.original.id, 'parent' : data.parent })
					.done(function (d) {
						//data.instance.load_node(data.parent);
						data.instance.refresh();
					})
					.fail(function () {
						data.instance.refresh();
					});
			})
			.on('changed.jstree', function (e, data) {
				if(data && data.selected && data.selected.length) {
					\$.get('?operation=get_content&id=' + data.selected.join(':'), function (d) {
						if(d && typeof d.type !== 'undefined') {
							\$('#data .content').hide();
							switch(d.type) {
								case 'text':
								case 'txt':
								case 'md':
								case 'htaccess':
								case 'log':
								case 'sql':
								case 'php':
								case 'js':
								case 'json':
								case 'css':
								case 'html':
									\$('#data .code').show();
									\$('#code').val(d.content);
									break;
								case 'png':
								case 'jpg':
								case 'jpeg':
								case 'bmp':
								case 'gif':
									\$('#data .image img').one('load', function () { \$(this).css({'marginTop':'-' + \$(this).height()/2 + 'px','marginLeft':'-' + \$(this).width()/2 + 'px'}); }).attr('src',d.content);
									\$('#data .image').show();
									break;
								default:
									\$('#data .default').html(d.content).show();
									break;
							}
						}
					});
				}
				else {
					\$('#data .content').hide();
					\$('#data .default').html('Select a file from the tree.').show();
				}
			});
	});
    </script>
  </body>
</html>

>>