package event.game.logicprocessing.expression;

import static model.tile.Tile.TILE_WIDTH;

import java.util.List;
import java.util.Queue;

import common.event.GameEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.actor.PositionalActor;
import model.tile.Tile;
import model.tile.TileChunk;

public class AreaDamageEvent extends CardExpressionEvent {

	private Tile target;
	private int range;

	public AreaDamageEvent(CardPlayer player, Tile target, int range) {
		super(player);
		this.target = target;
		this.range = range;
	}

	public AreaDamageEvent(long time, CardPlayer player, Tile target, int range) {
		super(time, player);
		this.target = target;
		this.range = range;
	}

	@Override
	public int priority() {
		return 7;
	}

	@Override
	public int processTime() {
		return 7;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		TileChunk targetedChunk = target.chunk();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				TileChunk chunk = state.tileMap().chunk(targetedChunk.pos().copy().add(j, i));
				List<PositionalActor> actors = state.actors(chunk.pos());
				for (PositionalActor actor : actors) {
					if (actor.relativePos(targetedChunk.pos(), target.pos()).lengthSquared() <= (range * TILE_WIDTH) * (range * TILE_WIDTH)) {
						Tile.tilePos(actor.pos());
						// TODO
					}
				}
			}
		}
	}

}
