package model.actor;

public abstract class HealthActor extends Actor {

	protected int health;
	protected int maxHealth;

	public HealthActor(int maxHealth) {
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}

	public HealthActor(long id, int maxHealth) {
		super(id);
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

	public <A extends HealthActor> A copyTo(A copy) {
		copy.maxHealth = maxHealth;
		copy.health = health;
		return super.copyTo(copy);
	}

}
