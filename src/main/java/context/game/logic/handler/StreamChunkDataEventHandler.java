package context.game.logic.handler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.network.p2p.game.StreamChunkDataEvent;

/**
 * Handles the {@link StreamChunkDataEvent} event.
 */
public class StreamChunkDataEventHandler implements Consumer<StreamChunkDataEvent> {

	private final NomadsGameData data;

	public StreamChunkDataEventHandler(NomadsGameData data) {
		this.data = data;
	}

	/**
	 * Sets the chunk data to be the data received in the event.
	 *
	 * @param e The StreamChunkDataEvent to handle.
	 */
	@Override
	public void accept(StreamChunkDataEvent e) {
		data.currentState().worldMap().addChunk(e.chunk());
		e.chunk().addActorsTo(data.currentState());
		data.tools().logMessage("Received chunk " + e.chunk().pos() + " from " + e.source());
	}

}
