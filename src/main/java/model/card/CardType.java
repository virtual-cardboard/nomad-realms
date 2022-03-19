package model.card;

public enum CardType {

	ACTION,
	CANTRIP,
	STRUCTURE,
	TASK;

	public final String name;

	private CardType() {
		this.name = toString().toLowerCase();
	}

}
