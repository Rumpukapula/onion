<div>
<h1>Player statistics</h1>

<div ng-controller="PlayerTableController">
<table ng-table="tableParams" class="table">
	<thead>
		<tr>
			<th>id</th>
			<th>name</th>
			<th>details</th>
		</tr>
	</thead>
	
	<tbody>
		<tr ng-repeat="player in $data">
            <td>{{player.id}}</td>
            <td>{{player.name}}</td>
	        <td><a class="btn btn-info" href="#/player-stats/{{player.id}}">show</a></td>
	    </tr> 
    </tbody>
</table>
</div>

</div>