'use strict';

var onionApp = angular.module('onionApp', [
   'ngRoute',
   'ngCookies',
   'ngResource',
   'highcharts-ng',
   'onionControllers',
   'ui.bootstrap',
   'ngTable'
]);

onionApp.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when('/player-stats',{templateUrl:'views/front-page.jsp', controller:'FrontPageController'})
		.when('/player-stats/:playerId',{templateUrl:'views/player-details.jsp',controller:'PlayerDetailController'})
		.when('/hand-stats',{templateUrl:'views/hand-stats.jsp',controller:'HandStatsController'})
		.otherwise({
			redirectTo:'/player-stats'
		});
}]);

onionApp.factory('Player', function($resource) {
	var Player = $resource('players/:playerId',{playerId:'@playerId'});
	return Player;
});

onionApp.factory('Hand', function($resource) {
	var Hand = $resource('hands');
	return Hand;
});

onionApp.factory('HoleCards', function($resource) {
	var HoleCards = $resource('holecards');
	return HoleCards;
});

onionApp.factory('Action', function($resource) {
	var Action = $resource('actions');
	return Action;
});