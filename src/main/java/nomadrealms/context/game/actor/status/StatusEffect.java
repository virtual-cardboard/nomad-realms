package nomadrealms.context.game.actor.status;

public enum StatusEffect {

	BURNED("burned"),
	FROZEN("frozen"),
	POISON("poison");

	private final String image;

	StatusEffect(String image) {
		this.image = image;
	}

	public String image() {
		return image;
	}

}
