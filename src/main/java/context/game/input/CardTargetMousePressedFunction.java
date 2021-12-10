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
import model.actor.GameObject;
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
		GameObject target = null;
		switch (card.effect().targetType) {
			// TODO
			case CHARACTER:
				Collection<Actor> actors = inputContext.data.state().actors();
				for (Actor actor : actors) {
					if (cursor.toVec2f().sub(actor.viewPos(inputContext.camera())).lengthSquared() <= 1600) {
						target = actor;
						break;
					}
				}
				break;
			case TILE:
				GameState state = inputContext.data.state();
				int cx = (int) (camera.chunkPos().x + (cursor.x + camera.pos().x) / CHUNK_PIXEL_WIDTH);
				int cy = (int) (camera.chunkPos().y + (cursor.y + camera.pos().y) / CHUNK_PIXEL_HEIGHT);
				TileChunk chunk = state.tileMap().chunk(new Vector2i(cx, cy));
				if (chunk != null) {
					Vector2i tilePos = tilePos(calculatePos(cursor, camera));
					if (tilePos.x < 0 || tilePos.y < 0) {
						break;
					}
					target = chunk.tile(tilePos);
				}
			default:
				break;
		}
		if (target != null) {
			inputContext.cardWaitingForTarget = null;
			return inputContext.playCard(card, target);
		}
		return null;
	}

	private Vector2f calculatePos(Vector2i cursor, GameCamera camera) {
		// The position in pixels from the top left corner of the current chunk
		float posX = (camera.pos().x + cursor.x + CHUNK_PIXEL_WIDTH) % CHUNK_PIXEL_WIDTH;
		float posY = (camera.pos().y + cursor.y + CHUNK_PIXEL_HEIGHT) % CHUNK_PIXEL_HEIGHT;
		return new Vector2f(posX, posY);
	};

}
