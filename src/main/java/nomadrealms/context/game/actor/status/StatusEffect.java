package nomadrealms.context.game.actor.status;

public enum StatusEffect {

	BURNED("burned.png"),
	FROZEN("frozen.png"),
	POISON("poison.png");

	private final String imageName;

	StatusEffect(String imageName) {
		this.imageName = imageName;
	}

	public String imageName() {
		return imageName;
	}

}
