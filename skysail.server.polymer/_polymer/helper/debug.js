
  function debug (msg, toConsole) {
	  if (toConsole) {
		  console.log(msg);
	  }
      var ajaxDebug = document.getElementById("ajaxDebug");
      if (ajaxDebug) {
    	  ajaxDebug.innerHTML += new Date().toLocaleTimeString() + ": " + msg + "<br>\n";
      }
  }