package fi.onion.model;

public class PlayerAction {
	int id;
	int hand_id;
	int phase_id;
	int action_sequence;
	int player_id;
	int action_id;
	float amount;
	boolean is_all_in;
	
	@Override
	public String toString() {
		return "Action - id:"+this.id+" - hand:"+this.hand_id+" - phase:"+this.phase_id+" - sequence:"+this.action_sequence+" - player:"+this.player_id+" - action:"+this.action_id+" - amount:"+this.amount;
	}
	
	public void setAllIn(boolean allIn) {
		this.is_all_in = allIn;
	}
	
	public boolean isAllIn() {
		return this.is_all_in;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public void setHandId(int id) {
		this.hand_id=id;
	}
	
	public int getHandId() {
		return this.hand_id;
	}
	
	public void setPhaseId(int id) {
		this.phase_id=id;
	}
	
	public int getPhaseId() {
		return this.phase_id;
	}
	
	public void setActionSeq(int id) {
		this.action_sequence=id;
	}
	
	public int getActionSeq() {
		return this.action_sequence;
	}
	
	public void setPlayerId(int id) {
		this.player_id=id;
	}
	
	public int getPlayerId() {
		return this.player_id;
	}
	
	public void setActionId(int id) {
		this.action_id=id;
	}
	
	public int getActionId() {
		return this.action_id;
	}
	
	public void setAmount(float amount) {
		this.amount=amount;
	}
	
	public float getAmount() {
		return this.amount;
	}
}
