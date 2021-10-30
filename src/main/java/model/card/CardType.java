package model.card;

public enum CardType {

	ACTION("action"),
	CANTRIP("cantrip"),
	CREATURE("creature"),
	STRUCTURE("structure");

	public final String name;

	private CardType(String name) {
		this.name = name;
	}

}
