package model.actor;

public class HealthActor {

	private int health;
	private int maxHealth;

	public HealthActor(int maxHealth) {
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}

	public int health() {
		return health;
	}

	public int maxHealth() {
		return maxHealth;
	}

	public boolean isDead() {
		return health <= 0;
	}

	public void changeHealth(int change) {
		health += change;
	}

}
