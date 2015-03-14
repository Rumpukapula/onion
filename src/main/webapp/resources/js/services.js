var onionServices = angular.module('onionServices',['ngResource']).factory('Player', ['$resource',
 function($resource) {
	var Player = $resource('/players', {playerId:'@id'},{update:{method:'PUT'}});
	Player.prototype.isNew = function() { return (typeof(this.id) === 'undefined');};
	return Player;
}]);