

todosProduct.controller('AppCtrl', function($scope, $rootScope, $http, $route, $modal, $log) {

	var defaultGetHeadersConfig = {
         	headers: {'Accept': 'application/json'} 
    }

    $rootScope.loggedIn = false;
	$rootScope.mode="init";

    $scope.currentUser = null;
	$scope.currentTodo;
	$scope.currentList;
	$scope.isNewNote = false;
	$scope.isNoteUpdate = false;
	$scope.isNewList = false;
	$scope.isListUpdate = false;
	$scope.parents;
	$scope.dirty = false;

	$scope.userform = {
	        user: '',
	        password: null
	};
	    
	$scope.$on('activeEditorChangeEvent', function() {
		$scope.isNoteUpdate = true;
		$scope.showUpdateButton()
	});
	    
    /* === Current User ============================================ */
    
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

    /* === Login / Logout ============================================ */
        
    $scope.logout = function () {
    	$http.get('/_logout')
    		.success(function() {
    	    	$scope.currentUser = null;
    	    	$rootScope.loggedIn = false;
    	    	$scope.currentTodo = null;
    	    	$scope.currentList = null;
    	    	window.location.reload();
    		})
    		.error(function(data,status,headers,config){
    			alert(status)
    		});
    };

	/* === List management =========================================== */

	$scope.getTodoLists = function() {
	    link = '../Todos/Lists';
		$http.get(link).success(function(json) {
			$scope.lists = json;
		});
	}
	
	$scope.setCurrentList = function(list) {
		$scope.currentList = list;
		$rootScope.mode="setList"
		$scope.getTodos()
	};

    $scope.newTodoList = function() {
    	var newList = {
    			id: null,
    	        name: 'new',
    	        desc: ''
    	    };
    	$scope.currentList = newList;
    	$scope.isNewList = true;
    	$rootScope.mode="list";
	};
	
	$scope.addTodoList = function() {
		 var data = JSON.stringify($scope.currentList);
		 var newId = null;
		 
		 $http.post('../Todos/Lists/', data, defaultGetHeadersConfig).
		 	success(function(data,status,headers,config) {
		 		if (status != 201) {
		 			alert("Sorry, there was a problem on the server!")
		 		} else {
		 			var segments = headers('Location').split("/");
		 			newId = "#" + segments[segments.length-1].replace("%3A",":");
		 			$rootScope.mode="empty";
		 			$scope.getTodoLists();
		 	    	$scope.currentList = {
		 	    			id: newId,
		 	    	        name: data.name,
		 	    	        desc: data.desc
		 	    	    };
		 		}
		 	}).
		 	error(function(data, status, headers, config){
	 			addFadingMessage(headers('X-status-reason'), "#actionMessage");
		 	});
	};

	$scope.editTodoList = function() {
    	$rootScope.mode="list";
    	$scope.isListUpdate=true;
	};
	
	$scope.deleteList = function(list) {
		if (list === undefined) {
			return;
		}
		var path = '../Todos/Lists/' + list.id.replace("#","");
		$http.delete(path).
		 	success(function(data,status,headers,config) {
	 	    	$scope.currentList = null;
	 			$scope.getTodoLists();
	 			// $scope.getTodos();
		 	}).
		 	error(function(data,status,headers,config){
		 		alert("Error (" + status + ") on Server.")
		 	});
	};
	
	 $scope.updateList = function() {
		 var link = '../Todos/Lists/' + $scope.currentList.id.replace("#","") + "/";
		 var desc = $scope.currentList.desc;

		 var data = "{\"id\":\""+$scope.currentList.id+"\", \"name\": \""+$scope.currentList.name+"\", \"desc\": \""+desc+"\"}";
		 
		 var config = {
        	headers: {'Content-Type': 'application/json'} 
        }

		 $http.patch(link, data,config).
		 	success(function(data,status,headers,config) {
	 			addFadingMessage("saved...", "#actionMessage");
		 	}).
		 	error(function(data,status,headers,config){
		 		alert("Failure on server! Code '"+status+"'");
		 	});
   };


	/* === Todo management =========================================== */

	 $scope.newTodo = function() {
	    	var newTodo = {
	    			id: null,
	    	        title: 'new todo',
	    	        desc: ''
	    	    };
	    	$scope.currentTodo = newTodo;
	    	$scope.isNewTodo = true;
	    	$rootScope.mode="newTodo";
		};
		
	$scope.getTodos = function() {
		listId = $scope.currentList.id.replace("#","");
	    link = '../Todos/Lists/'+listId+'/Todos';
		$http.get(link).success(function(json) {
			$scope.todos = json;
		});
	}
		
	$scope.getTodoLists()
	
	$scope.setCurrentTodo = function(todo) {
		$scope.currentTodo = todo;
		$rootScope.mode="todo";
		$scope.getNextStatuses(todo.status);
	};

	$scope.deleteNote = function() {
		if ($scope.currentTodo === undefined) {
			return;
		}
		var path = '../notes/notes/' + $scope.currentTodo.id;
		$http.delete(path).
		 	success(function(data,status,headers,config) {
		 		if (!data.success) {
		 			alert("Sorry, there was a problem on the server!")
		 		} else {
		 	    	$scope.currentTodo = null;
		 			$scope.getTodoLists()
		 		}
		 	}).
		 	error(function(data,status,headers,config){
		 		alert("Error (" + status + ") on Server.")
		 	});
	};
	
	$scope.showDeleteButton = function () {
		return ($scope.currentTodo != null) && !$scope.isNewNote;
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
	
	$scope.showTodo = function() {
		return $rootScope.loggedIn && $rootScope.mode=='todo';
	}
	
	$scope.showNewList = function() {
		return $rootScope.loggedIn && $rootScope.mode=='list';
	}
	
	$scope.showNewTodo = function() {
		return $rootScope.loggedIn && $rootScope.mode=='newTodo';
	}
	
	$scope.showAddListSign = function () {
		return $rootScope.mode!='list' && $rootScope.mode!='newTodo';
	}

	$scope.showAddTodoSign = function () {
		return $scope.currentList != null && $rootScope.mode!='list' && $rootScope.mode!='newTodo';
	}
	
	$scope.showListUpdateButton = function () {
		if ($scope.isNewList) {
			return false;
		}
		return $scope.isListUpdate;
	}	
	
});



