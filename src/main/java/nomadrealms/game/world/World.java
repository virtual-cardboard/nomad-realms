package nomadrealms.game.world;

import nomadrealms.game.GameState;
import nomadrealms.game.actor.Actor;
import nomadrealms.game.actor.Farmer;
import nomadrealms.game.actor.HasPosition;
import nomadrealms.game.actor.Nomad;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.event.InputEventFrame;
import nomadrealms.game.event.ProcChain;
import nomadrealms.game.item.WorldItem;
import nomadrealms.game.world.map.Chunk;
import nomadrealms.game.world.map.tile.Tile;
import nomadrealms.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nomadrealms.game.item.Item.OAK_LOG;
import static nomadrealms.game.item.Item.WHEAT_SEED;

public class World {

	private GameState state;

	private Chunk chunk;
	public Nomad nomad;
	public List<Actor> actors;
	public Map<Tile, HasPosition> tileToEntityMap;

	public List<ProcChain> procChains = new ArrayList<>();

	public World(GameState state) {
		this.state = state;
		chunk = new Chunk();
		nomad = new Nomad("Donny", getTile(1, 0));
		nomad.inventory().add(new WorldItem(OAK_LOG));
		nomad.inventory().add(new WorldItem(WHEAT_SEED));
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
			}
		}
		procChains.removeIf(ProcChain::empty);
		for (ProcChain chain : procChains) {
			chain.update();
		}
	}

	public void resolve(InputEvent event) {
		System.out.println("You should override the double visitor pattern resolve method in "
				+ event.getClass().getSimpleName() + " and add a resolve method in World.");
	}

	public void resolve(CardPlayedEvent event) {
		Deck deck = (Deck) event.card().zone();
		deck.removeCard(event.card());
		List<Intent> intents = event.card().card().expression().intents(this, event.target(), event.source());
		procChains.add(new ProcChain(this, intents));
		deck.addCard(event.card());
		state.uiEventChannel.add(event);
	}

	public Tile getTile(int row, int col) {
		return chunk.getTile(row, col);
	}

	public HasPosition getTargetOnTile(Tile tile) {
		return tileToEntityMap.get(tile);
	}

	public void setTile(Tile tile) {
		chunk.setTile(tile);
	}
}
