package model.actor.health;

import java.util.function.BiPredicate;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import model.GameObject;
import model.actor.Actor;
import model.actor.health.cardplayer.CardPlayer;

public abstract class HealthActor extends Actor {

	protected int health;
	protected int maxHealth;

	public HealthActor() {
	}

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

	@Override
	public boolean shouldRemove() {
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

	public static BiPredicate<CardPlayer, GameObject> isHealthActor() {
		return (c, t) -> t instanceof HealthActor;
	}

	@Override
	public void read(SerializationReader reader) {
		super.read(reader);
		this.health = reader.readInt();
		this.maxHealth = reader.readInt();
	}

	@Override
	public void write(SerializationWriter writer) {
		super.write(writer);
		writer.consume(health);
		writer.consume(maxHealth);
	}

}
