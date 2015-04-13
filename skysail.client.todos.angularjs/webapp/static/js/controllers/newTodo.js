todosProduct.controller('NewTodoCtrl', function($scope,$http) {
	
	var defaultGetHeadersConfig = {
         	headers: {'Accept': 'application/json'} 
    }
	
	$scope.addTodo = function() {
		
		 var data = JSON.stringify($scope.currentTodo);
		 var link = '../Todos/Lists/' + $scope.currentList.id.replace("#","") + '/Todos/';
		 
		 $http.post(link, data, defaultGetHeadersConfig).
		 	success(function(data,status,headers,config) {
		 		if (status != 201) {
		 			alert("Sorry, there was a problem on the server!")
		 		}
	 			$scope.getTodoLists();
	 			$scope.getTodos();
	 			$scope.mode="init";
		 	}).
		 	error(function(data, status, headers, config){
	 			addFadingMessage(headers('X-status-reason'), "#actionMessage");
		 	});
	};

	
});
