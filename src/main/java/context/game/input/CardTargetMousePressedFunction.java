package context.game.input;

import static model.tile.Tile.tilePos;
import static model.tile.TileChunk.CHUNK_HEIGHT;
import static model.tile.TileChunk.CHUNK_SIDE_LENGTH;
import static model.tile.TileChunk.CHUNK_WIDTH;
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

	private NomadsInputInfo inputInfo;

	public CardTargetMousePressedFunction(NomadsInputInfo inputInfo) {
		this.inputInfo = inputInfo;
	}

	@Override
	public GameEvent apply(MousePressedInputEvent t) {
		if (inputInfo.cardWaitingForTarget == null || t.button() != GLFW_MOUSE_BUTTON_LEFT) {
			return null;
		}
		GameCard card = inputInfo.cardWaitingForTarget.card();
		Vector2i cursor = inputInfo.cursor.pos();
		GameCamera camera = inputInfo.camera();
		GameObject target = null;
		switch (card.effect().targetType) {
			// TODO
			case CHARACTER:
				Collection<Actor> actors = inputInfo.data.state().actors();
				for (Actor actor : actors) {
					if (cursor.toVec2f().sub(actor.screenPos(inputInfo.camera(), inputInfo.settings)).lengthSquared() <= 1600) {
						target = actor;
						break;
					}
				}
				break;
			case TILE:
				GameState state = inputInfo.data.state();
				int cx = (int) (camera.chunkPos().x + Math.floor((cursor.x / inputInfo.settings.worldScale + camera.pos().x) / CHUNK_WIDTH));
				int cy = (int) (camera.chunkPos().y + Math.floor((cursor.y / inputInfo.settings.worldScale + camera.pos().y) / CHUNK_HEIGHT));
				TileChunk chunk = state.worldMap().chunk(new Vector2i(cx, cy));
				if (chunk != null) {
					Vector2i tilePos = tilePos(calculatePos(cursor, camera));
					if (tilePos.x == -1) {
						chunk = state.worldMap().chunk(new Vector2i(cx - 1, cy));
						tilePos = new Vector2i(CHUNK_SIDE_LENGTH - 1, tilePos.y);
					}
					target = chunk.tile(tilePos);
				}
			default:
				break;
		}
		if (target != null && card.effect().condition.test(inputInfo.data.player(), target)) {
			inputInfo.cardWaitingForTarget = null;
			return inputInfo.playCard(card, target);
		}
		return null;
	}

	private Vector2f calculatePos(Vector2i cursor, GameCamera camera) {
		// The position in pixels from the top left corner of the current chunk
		float posX = (camera.pos().x + cursor.x / inputInfo.settings.worldScale + CHUNK_WIDTH) % CHUNK_WIDTH;
		float posY = (camera.pos().y + cursor.y / inputInfo.settings.worldScale + CHUNK_HEIGHT) % CHUNK_HEIGHT;
		return new Vector2f(posX, posY);
	};

}
