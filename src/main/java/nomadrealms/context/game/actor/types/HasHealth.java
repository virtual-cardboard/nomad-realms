package nomadrealms.context.game.actor.types;

public interface HasHealth {

	int health();

	void health(int health);

	default void damage(int damage) {
		health(health() - damage);
	}

	default void heal(int amount) {
		health(health() + amount);
	}

}
