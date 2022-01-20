package context.game.input;

import static model.world.Tile.tileCoords;
import static model.world.TileChunk.CHUNK_SIDE_LENGTH;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import java.util.Collection;
import java.util.function.Function;

import common.event.GameEvent;
import common.math.Vector2f;
import common.math.Vector2i;
import context.game.visuals.GameCamera;
import context.input.event.MousePressedInputEvent;
import model.actor.Actor;
import model.actor.GameObject;
import model.card.WorldCard;
import model.state.GameState;
import model.world.layer.finallayer.FinalLayerChunk;

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
		long cardID = inputInfo.cardWaitingForTarget.cardID();
		WorldCard card = inputInfo.card(cardID);
		Vector2i cursor = inputInfo.cursor.pos();
		GameCamera camera = inputInfo.camera();
		GameState state = inputInfo.data.states().peekLast();

		GameObject target = null;
		switch (card.effect().targetType) {
			// TODO
			case CHARACTER:
				Collection<Actor> actors = state.actors().values();
				for (Actor actor : actors) {
					if (cursor.toVec2f().sub(actor.screenPos(inputInfo.camera(), inputInfo.settings)).lengthSquared() <= 1600) {
						target = actor;
						break;
					}
				}
				break;
			case TILE:
				float chunkWidth = inputInfo.settings.chunkWidth();
				float chunkHeight = inputInfo.settings.chunkHeight();
				int cx = (int) (camera.chunkPos().x + Math.floor((cursor.x + camera.pos().x) / chunkWidth));
				int cy = (int) (camera.chunkPos().y + Math.floor((cursor.y + camera.pos().y) / chunkHeight));
				FinalLayerChunk chunk = state.worldMap().finalLayerChunk(new Vector2i(cx, cy));
				if (chunk != null) {
					Vector2i tilePos = tileCoords(calculatePos(cursor, camera), inputInfo.settings);
					if (tilePos.x == -1) {
						chunk = state.worldMap().finalLayerChunk(new Vector2i(cx - 1, cy));
						tilePos = new Vector2i(CHUNK_SIDE_LENGTH - 1, tilePos.y);
					}
					target = chunk.tile(tilePos);
				}
			default:
				break;
		}
		if (target != null) {
			boolean meetsCondition = card.effect().condition.test(inputInfo.data.nextState().cardPlayer(inputInfo.data.playerID()), target);
			if (meetsCondition) {
				inputInfo.cardWaitingForTarget = null;
				return inputInfo.playCard(cardID, target);
			}
		}
		return null;
	}

	private Vector2f calculatePos(Vector2i cursor, GameCamera camera) {
		float chunkWidth = inputInfo.settings.chunkWidth();
		float chunkHeight = inputInfo.settings.chunkHeight();
		// The position in pixels from the top left corner of the current chunk
		float posX = (camera.pos().x + cursor.x + chunkWidth) % chunkWidth;
		float posY = (camera.pos().y + cursor.y + chunkHeight) % chunkHeight;
		return new Vector2f(posX, posY);
	};

}
