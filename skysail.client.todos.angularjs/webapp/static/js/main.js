var notesProduct = angular.module('notesProduct', ['ui.bootstrap','ngRoute','filters']);


notesProduct.controller('AppCtrl', function($scope, $rootScope, $http, $route, $modal, $log) {

	var defaultGetHeadersConfig = {
         	headers: {'Accept': 'application/json'} 
    }
	
    $scope.currentUser = null;
    $rootScope.loggedIn = false;
	$scope.currentNote;
	$scope.isNewNote = false;
	$scope.isNoteUpdate = false;
	$scope.parents;

	$scope.userform = {
	        user: '',
	        password: null
	};
	    
	$scope.$on('activeEditorChangeEvent', function() {
		$scope.isNoteUpdate = true;
		$scope.showUpdateButton()
	});
	    
    /* === Name  ============================================ */
    
    $http.get('/usermanagement/currentUser',defaultGetHeadersConfig)
    	.success(function(json) {
    		if (json.username) {
    			$scope.currentUser = json.username;
				$scope.userform.user = json.username;
				$rootScope.loggedIn = true;
    		}
    	 })
    	.error(function(data,status,headers,config){
        	alert(status)
        })
    
    $scope.logout = function () {
    	$http.get('/_logout')
    		.success(function() {
    	    	$scope.currentUser = null;
    	    	$rootScope.loggedIn = false;
    	    	$scope.currentNote = null;
    	    	window.location.reload();
    		})
    		.error(function(data,status,headers,config){
    			alert(status)
    		});
    };
    
	$scope.showDeleteButton = function () {
		return ($scope.currentNote != null) && !$scope.isNewNote;
	}
	
	$scope.setDirty = function () {
		$scope.isNoteUpdate = true;
	}
	
	$scope.showUpdateButton = function () {
		if ($scope.isNewNote) {
			return false;
		}
		return $scope.isNoteUpdate;
	}

	/* === Clips management =========================================== */

	$scope.getNotes = function() {
	    link = '../clipboard/clips';
		$http.get(link).success(function(json) {
			$scope.notes = json;
		});
	}
	
	$scope.getNote = function(noteId) {
		link = '../clipboard/clips/' + noteId;
		$http.get(link).success(function(json) {
			if (json.success) {
				alert(json);
				$scope.currentNote = json;
			} else {
				alert ("Sorry, there was an error on the server: " + data.message);
			}
		});
	}
	
	$scope.getNotes()
	
	$scope.setCurrentNote = function(note) {
		$scope.currentNote = note;
	};
	
    $scope.newNote = function() {
    	var parentId = null;
    	var newNote = {
    			id: null,
    	        title: 'new',
    	        content: '',
    	        parent: parentId
    	    };
    	$scope.currentNote = newNote;
    	$scope.isNewNote = true;
	};
	
	$scope.addNote = function() {
		 var content = getContentFromEditor();
		 
		 var data = "title="+$scope.currentNote.title+"&content="+content+"&parent="+$scope.currentNote.parent;
		 var config = {
         	headers: {'Content-Type': 'application/x-www-form-urlencoded'} 
         }
		 
		 $http.post('../notes/notes/', data, config).
		 	success(function(data,status,headers,config) {
		 		if (!data.success) {
		 			alert("Sorry, there was a problem on the server!")
		 		} else {
		 			$scope.isNewNote = false;
		 			$scope.getNotes()
		 			$scope.getNote(data.data.id)
		 		}
		 	}).
		 	error(function(status,headers,config){
		 		alert(status);
		 		alert(data);
		 	});
	};

    $scope.updateNote = function() {
		 var noteLink = '../clipboard/clips/' + $scope.currentNote["@rid"].replace("#","") + "/";
		 var content = $scope.currentNote.content;

		 var data = "{\"content\": \""+content+"\"}";
		 
		 var config = {
         	headers: {'Content-Type': 'application/json'} 
         }

		 $http.put(noteLink, data,config).
		 	success(function(data,status,headers,config) {
	 			addFadingMessage("saved...", "#actionMessage");
		 	}).
		 	error(function(data,status,headers,config){
		 		alert("Failure on server! Code '"+status+"'");
		 	});
    };
	 
	$scope.deleteNote = function() {
		if ($scope.currentNote === undefined) {
			return;
		}
		var path = '../notes/notes/' + $scope.currentNote.id;
		$http.delete(path).
		 	success(function(data,status,headers,config) {
		 		if (!data.success) {
		 			alert("Sorry, there was a problem on the server!")
		 		} else {
		 	    	$scope.currentNote = null;
		 			$scope.getNotes()
		 		}
		 	}).
		 	error(function(data,status,headers,config){
		 		alert("Error (" + status + ") on Server.")
		 	});
	};

	/* === Drag & Drop =========================================== */
	
	$scope.dropped = function(dragEl, dropEl) {
		var droppedTo = angular.element(dropEl);
		var dragged = angular.element(dragEl);
		//alert("drag to: folder #" + draggedToFolder[0].id);
		//alert("drop note #: " + droppedNote[0].id);
	
		//clear the previously applied color, if it exists
		var bgClass = droppedTo.attr('data-color');
		if (bgClass) {
			droppedNote.removeClass(bgClass);
		}

		//add the dragged color
		bgClass = dragged.attr("data-color");
		droppedTo.addClass(bgClass);
		droppedTo.attr('data-color', bgClass);

		//if element has been dragged from the grid, clear dragged color
		if (dragged.attr("x-lvl-drop-target")) {
			dragged.removeClass(bgClass);
		}
		
		var noteLink = '../notes/notes/' + dragged[0].id;
		//alert(noteLink)
		
		$http.get(noteLink)
			.success(
				function(json) { 
					//var data = "{ \"title\": \""+json.data.title+"\", \"content\": \""+json.data.content+"\", \"parent\":\""+droppedTo[0].id+"\"}";
					//alert(data);
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
					 			$scope.getNotes();
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

});

angular.module('filters', []).
    
    filter('truncate', function () {
        return function (text, length, end) {
            if (isNaN(length))
                length = 10;

            if (end === undefined)
                end = "...";

            if (text.length <= length || text.length - end.length <= length) {
                return text;
            }
            else {
                return String(text).substring(0, length-end.length) + end;
            }

    };
});
