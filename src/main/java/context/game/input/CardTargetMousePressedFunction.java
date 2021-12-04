package context.game.input;

import static model.tile.Tile.tilePos;
import static model.tile.TileChunk.CHUNK_PIXEL_HEIGHT;
import static model.tile.TileChunk.CHUNK_PIXEL_WIDTH;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import java.util.Collection;
import java.util.function.Function;

import common.event.GameEvent;
import common.math.Vector2f;
import common.math.Vector2i;
import context.game.visuals.GameCamera;
import context.input.event.MousePressedInputEvent;
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
		if (inputContext.cardWaitingForTarget == null || t.button() != GLFW_MOUSE_BUTTON_LEFT) {
			return null;
		}
		GameCard card = inputContext.cardWaitingForTarget.card();
		Vector2i cursor = inputContext.cursor.pos();
		GameCamera camera = inputContext.camera();
		Actor target = null;
		switch (card.effect().targetType) {
			// TODO
			case CHARACTER:
				Collection<Actor> actors = inputContext.data.state().actors();
				for (Actor actor : actors) {
					if (actor instanceof PositionalActor) {
						PositionalActor posActor = (PositionalActor) actor;
						if (cursor.toVec2f().sub(posActor.viewPos(inputContext.camera())).lengthSquared() <= 1600) {
							target = posActor;
							break;
						}
					}
				}
				break;
			case TILE:
				GameState state = inputContext.data.state();
				int cx = (int) (camera.chunkPos().x + (cursor.x + camera.pos().x) / CHUNK_PIXEL_WIDTH);
				int cy = (int) (camera.chunkPos().y + (cursor.y + camera.pos().y) / CHUNK_PIXEL_HEIGHT);
				TileChunk chunk = state.tileMap().chunk(new Vector2i(cx, cy));
				Vector2i tilePos = tilePos(new Vector2f((camera.pos().x + cursor.x) % CHUNK_PIXEL_WIDTH, (camera.pos().y + cursor.y) % CHUNK_PIXEL_HEIGHT));
				target = chunk.tile(tilePos);
				System.out.println("Targeted tile at chunk (" + cx + ", " + cy + ") tile " + tilePos);
			default:
				break;
		}
		if (target != null) {
			inputContext.cardWaitingForTarget = null;
			return inputContext.playCard(card, target);
		}
		return null;
	};

}
