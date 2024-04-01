package nomadrealms.game.card;

public class GameCard implements Card {

	public static final GameCard MOVE = new GameCard("Move", "Move to target hexagon");
	public static final GameCard ATTACK = new GameCard("Attack", "Deal 2 to target character");
	public static final GameCard HEAL = new GameCard("Heal", "Restore 2 to target character");

	private String name;
	private String description;

	public GameCard(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

}
