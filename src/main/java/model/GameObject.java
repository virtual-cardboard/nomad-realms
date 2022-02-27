package model;

import java.util.Random;

import math.IDGenerator;
import model.id.ID;
import model.state.GameState;

/**
 * Any object in the game that can be visually represented.
 * 
 * @author Jay
 */
public abstract class GameObject {

	protected long id;

	public GameObject() {
		id = genID();
	}

	public GameObject(long id) {
		this.id = id;
	}

	protected long genID() {
		return IDGenerator.genID();
	}

	public abstract ID id();

	public final Random random(long tick) {
		long seed = (tick << 32) + (int) id;
		System.out.println(seed);
		return new Random(seed);
	}

	public final long longID() {
		return id;
	}

	public abstract GameObject copy();

	public abstract String description();

	public abstract void addTo(GameState state);

}
