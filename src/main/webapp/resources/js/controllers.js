'use strict';

var onionControllers = angular.module('onionControllers', []);

onionControllers.controller('FrontPageController', ['$scope',
    function($scope) {

}]);

onionControllers.controller('PlayerDetailController',['$scope','$routeParams','$location','Player',
    function($scope,$routeParams,$location,Player) {
		var playerId = $routeParams.playerId;
		
		$scope.playerStats = Player.get({playerId:playerId}, function() {
			$scope.player = $scope.playerStats.player;
			$scope.hands = $scope.playerStats.hands;
			
			console.log($scope.playerStats);
			
			var preflopActions = [];
		});
		
		$scope.chartConfig = {
	        options: {
	            chart: {
	                type: 'bar'
	            }
	        },
	        series: [{
	            data: [10, 15, 12, 8, 7]
	        }],
	        title: {
	            text: 'Hello'
	        },
	
	        loading: false
	    };
}]);

onionControllers.controller('PlayerTableController',['$scope','Player','ngTableParams',
   function($scope,Player,ngTableParams) {
	var data = [];
	

	$scope.players = Player.query(function() {
		data = $scope.players;
		$scope.tableParams = new ngTableParams({
	        page: 1,            // show first page
	        count: 10           // count per page
	    }, {
	        total: data.length, // length of data
	        getData: function($defer, params) {
	            $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
	        }
	    });
	});
}]);

onionControllers.controller('TestController', ['$scope','$routeParams','$location',
   function($scope,$routeParams,$location) {
	$scope.list = [
       {'name':'Test',
	    'text':'Testee'},
	   
	   {'name':'Tester',
	    'text':'testen'} 
	];
	
	$scope.hello = "Greetz!";
}]);

onionControllers.controller('HandStatsController', ['$scope','$routeParams','$location',
   function($scope,$routeParams,$location) {
	$scope.list = [
       {'name':'Test',
	    'text':'Testee'},
	   
	   {'name':'Tester',
	    'text':'testen'} 
	];
	
	$scope.hello = "Greetz!";
}]);