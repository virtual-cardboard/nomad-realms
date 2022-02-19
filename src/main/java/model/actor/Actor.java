package model.actor;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import common.math.Vector2f;
import context.game.visuals.GameCamera;
import graphics.displayer.ActorDisplayer;
import math.WorldPos;
import model.GameObject;
import model.item.ItemCollection;
import model.state.GameState;

public abstract class Actor extends GameObject {

	protected WorldPos worldPos = new WorldPos();
	protected Vector2f direction = new Vector2f(0, 1);

	private boolean shouldRemove;

	public Actor() {
	}

	public Actor(long id) {
		super(id);
	}

	@Override
	public void addTo(GameState state) {
		state.actors().put(id, this);
		List<Actor> list = state.chunkToActors().get(worldPos.chunkPos());
		if (list == null) {
			list = new ArrayList<>();
			state.chunkToActors().put(worldPos.chunkPos(), list);
		}
		list.add(this);
	}

	public <A extends Actor> A copyTo(A copy) {
		copy.id = id;
		copy.worldPos = worldPos.copy();
		copy.direction = direction;
		copy.setShouldRemove(shouldRemove);
		return copy;
	}

	public WorldPos worldPos() {
		return worldPos;
	}

	public abstract ActorDisplayer<?> displayer();

	public abstract void update(GameState state);

	public final Vector2f screenPos(GameCamera camera, NomadsSettings s) {
		return worldPos.screenPos(camera, s);
	}

	public boolean shouldRemove() {
		return shouldRemove;
	}

	public void setShouldRemove(boolean shouldRemove) {
		this.shouldRemove = shouldRemove;
	}

	public ItemCollection dropItems() {
		return new ItemCollection();
	}

}
