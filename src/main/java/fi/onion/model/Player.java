package fi.onion.model;

public class Player {
	int id;
	String name;
	
	public Player(int id,String name) {
		this.id=id;
		this.name=name;
	}
	
	public Player(String name) {
		this.id=-1;
		this.name=name;
	}

	public Player() {
		
	}

	@Override
	public String toString() {
		return "Player - id:"+this.id+" - name:"+this.name;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
