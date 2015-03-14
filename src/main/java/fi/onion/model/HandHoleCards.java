package fi.onion.model;

public class HandHoleCards {
	int id;
	int hand_number;
	int player_id;
	String holecard1;
	String holecard2;
	boolean is_public;
	
	@Override
	public String toString() {
		return "Hole cards - id:"+this.id+" - hand number:"+this.hand_number+" - player:"+this.player_id+" - cards:"+this.holecard1+" "+this.holecard2+" - public:"+this.is_public;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public void setHandNumber(int id) {
		this.hand_number=id;
	}
	
	public void setPlayerId(int id) {
		this.player_id=id;
	}
	
	public void setHole1(String id) {
		this.holecard1=id;
	}
	
	public String getHole1() {
		return this.holecard1;
	}
	
	public void setHole2(String id) {
		this.holecard2=id;
	}
	
	public String getHole2() {
		return this.holecard2;
	}
	
	public void setPublic(boolean pub) {
		this.is_public = pub;
	}
	
	public boolean isPublic() {
		return this.is_public;
	}
	
	public String getHoleCards() {
		return ""+this.holecard1+"-"+this.holecard2;
	}
	
	public int getHandNumber() {
		return this.hand_number;
	}
	
	public int getPlayerId() {
		return this.player_id;
	}
}
