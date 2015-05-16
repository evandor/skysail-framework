todosProduct.controller('UpdateTodoCtrl', function($scope,$rootScope,$http) {
	
	var defaultGetHeadersConfig = {
         	headers: {'Accept': 'application/json'} 
    }
	
	$scope.updateTodo = function() {
		
		 var data = JSON.stringify($scope.currentTodo, ["id", "title", "desc" ]);
		 var link = '../Todos/Lists/' + $scope.currentList.id.replace("#","") + '/Todos/' + $scope.currentTodo.id.replace("#", "") + "/";
		 
		 $http.put(link, data, defaultGetHeadersConfig).
		 	success(function(data,status,headers,config) {
	 			$scope.getTodoLists();
	 			$scope.getTodos();
	 			$rootScope.mode="init";
		 	}).
		 	error(function(data, status, headers, config){
	 			addFadingMessage(headers('X-status-reason'), "#actionMessage");
	 			alert("calling PUT on link " + link + " failed...");
		 	});
	};
	
});
