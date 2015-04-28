$scope.dropped = function(dragEl, dropEl) {
		var droppedTo = angular.element(dropEl);
		var dragged = angular.element(dragEl);
		// alert("drag to: folder #" + draggedToFolder[0].id);
		// alert("drop note #: " + droppedNote[0].id);
	
		// clear the previously applied color, if it exists
		var bgClass = droppedTo.attr('data-color');
		if (bgClass) {
			droppedNote.removeClass(bgClass);
		}

		// add the dragged color
		bgClass = dragged.attr("data-color");
		droppedTo.addClass(bgClass);
		droppedTo.attr('data-color', bgClass);

		// if element has been dragged from the grid, clear dragged color
		if (dragged.attr("x-lvl-drop-target")) {
			dragged.removeClass(bgClass);
		}
		
		var noteLink = '../notes/notes/' + dragged[0].id;
		// alert(noteLink)
		
		$http.get(noteLink)
			.success(
				function(json) { 
					var data = "title="+json.data.title+"&content="+json.data.content+"&parent="+droppedTo[0].id;
	                var config = {
	                 	headers: {'Content-Type': 'application/x-www-form-urlencoded'} 
	                }
					$http.put(noteLink, data, config).
					 	success(function(data,status,headers,config) {
					 		if (!data.success) {
					 			alert("error happened: " + data.message);
					 		} else {
					 			addFadingMessage("moved...", "#actionMessage");
					 			$scope.getTodoLists();
					 		}
					 	}).
					 	error(function(data,status,headers,config){
					 		alert("Failure on server! Code '"+status+"'");
					 	});
				})
			.error(
				function(status) { 
					alert(status) 
				});
	}
