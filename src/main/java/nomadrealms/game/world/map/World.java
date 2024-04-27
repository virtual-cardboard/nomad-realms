package nomadrealms.game.world.map;

import nomadrealms.game.GameState;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.event.InputEventFrame;
import nomadrealms.game.world.actor.Actor;
import nomadrealms.game.world.actor.Farmer;
import nomadrealms.game.world.actor.HasPosition;
import nomadrealms.game.world.actor.Nomad;
import nomadrealms.game.world.map.tile.Tile;
import nomadrealms.render.RenderingEnvironment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {

	private GameState state;

	private Chunk chunk;
	public Nomad nomad;
	public List<Actor> actors;
	public Map<Tile, HasPosition> tileToEntityMap;

//	public List<ProcChain> procChains = new ArrayList<>();

	public World(GameState state) {
		this.state = state;
		chunk = new Chunk();
		nomad = new Nomad("Donny", getTile(1, 0));
		Farmer farmer = new Farmer("Joe", getTile(1, 5));
		actors = List.of(nomad, farmer);
	}

	public Chunk getChunk() {
		return chunk;
	}

	public void renderMap(RenderingEnvironment re) {
		chunk.render(re);
		nomad.render(re);
	}

	public void renderActors(RenderingEnvironment re) {
		for (Actor entity : actors) {
			entity.render(re);
		}
	}

	int x = 0;
	int i = 0;

	public void update(InputEventFrame inputEventFrame) {
		i++;
		if (i % 10 == 0) {
			x = Math.min(x + 1, 15);
			nomad.tile(getTile(x, 2));
			i = 0;
		}
		tileToEntityMap = new HashMap<>();
		for (HasPosition entity : actors) {
			tileToEntityMap.put(entity.tile(), entity);
		}
		for (Actor actor : actors) {
			actor.update(this.state);
			for (InputEvent event : actor.retrieveNextPlays()) {
				event.resolve(this);
				System.out.println("Double resolve " + event.getClass().getSimpleName() + " " + event);
			}
		}
	}

	public void resolve(InputEvent inputEvent) {
		System.out.println("You should override the double visitor pattern resolve method in "
				+ inputEvent.getClass().getSimpleName() + " and add a resolve method in World.");
	}

	public void resolve(CardPlayedEvent event) {
		System.out.println("yey");
	}

	public Tile getTile(int row, int col) {
		return chunk.getTile(row, col);
	}

	public HasPosition getTargetOnTile(Tile tile) {
		return tileToEntityMap.get(tile);
	}
}
