package context.game.input;

import static java.lang.Math.floor;
import static model.tile.Tile.TILE_HEIGHT;
import static model.tile.Tile.TILE_WIDTH;
import static model.tile.TileChunk.CHUNK_PIXEL_HEIGHT;
import static model.tile.TileChunk.CHUNK_PIXEL_WIDTH;

import java.util.Collection;
import java.util.function.Function;

import common.event.GameEvent;
import common.math.Vector2i;
import context.game.visuals.GameCamera;
import context.input.event.MousePressedInputEvent;
import model.GameObject;
import model.GameState;
import model.actor.Actor;
import model.actor.PositionalActor;
import model.card.GameCard;
import model.tile.TileChunk;

public class CardTargetMousePressedFunction implements Function<MousePressedInputEvent, GameEvent> {

	private NomadsGameInputContext inputContext;

	public CardTargetMousePressedFunction(NomadsGameInputContext inputContext) {
		this.inputContext = inputContext;
	}

	@Override
	public GameEvent apply(MousePressedInputEvent t) {
		if (inputContext.cardWaitingForTarget == null) {
			return null;
		}
		inputContext.cardWaitingForTarget.setLockTargetPos(false);
		GameCard card = inputContext.cardWaitingForTarget.card();
		Vector2i cursor = inputContext.cursor.pos();
		GameCamera camera = inputContext.camera();
		GameObject target = null;
		switch (card.effect().targetType) {
			// TODO
			case CHARACTER:
				Collection<Actor> actors = inputContext.data.state().actors();
				for (Actor actor : actors) {
					if (actor instanceof PositionalActor) {
						PositionalActor posActor = (PositionalActor) actor;
						if (cursor.toVector2f().sub(posActor.viewPos(inputContext.camera())).lengthSquared() <= 1600) {
							target = posActor;
							break;
						}
					}
				}
				break;
			case TILE:
				GameState state = inputContext.data.state();
				int cx = camera.chunkPos().x + cursor.x / CHUNK_PIXEL_WIDTH;
				int cy = camera.chunkPos().y + cursor.y / CHUNK_PIXEL_HEIGHT;
				int tx = (int) floor(((camera.pos().x + cursor.x) % CHUNK_PIXEL_WIDTH) / TILE_WIDTH);
				int ty = (int) floor(((camera.pos().y + cursor.y) % CHUNK_PIXEL_HEIGHT) / TILE_HEIGHT);
				TileChunk chunk = state.tileMap().chunk(new Vector2i(cx, cy));
				target = chunk.tile(tx, ty);
				System.out.println("Targeted tile at chunk (" + cx + ", " + cy + ") tile (" + tx + ", " + ty + ").");
			default:
				break;
		}
		if (target != null) {
			inputContext.cardWaitingForTarget.setLockTargetPos(false);
			inputContext.cardWaitingForTarget = null;
			return inputContext.playCard(card, target);
		}
		return null;
	};

}
