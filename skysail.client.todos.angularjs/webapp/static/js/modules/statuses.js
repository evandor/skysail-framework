var statusesModule = angular.module('statuses', [])

//	.value('statusesService', {
//		getNextStatuses: function(currentStatus) {
//		  link = '../Todos/Statuses/' + currentStatus;
//			$http.get(link).success(function(json) {
//				$scope.nextStatuses = json;
//				alert(json);
//			}).error(function() {alert("error")});
//	    }
//	});

.controller('StatusesController', function($scope, $http){
	$scope.getNextStatuses = function(currentStatus) {
		link = '../Todos/Statuses/' + currentStatus;
		$http.get(link).success(function(json) {
			$scope.nextStatuses = json;
			alert(json);
		}).error(function() {alert("error")});
	}
});
