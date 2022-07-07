package context.game;

import java.util.List;

import app.NomadsSettings;
import context.data.GameData;
import context.game.data.Tools;
import context.game.visuals.GameCamera;
import engine.common.networking.packet.address.PacketAddress;
import engine.common.time.GameTime;
import model.card.CardCollection;
import model.id.CardPlayerId;
import model.state.GameState;
import model.state.LimitedStack;
import networking.GameNetwork;

public class NomadsGameData extends GameData {

	private NomadsSettings settings = new NomadsSettings(48f, 0.375f, 1, 1, 1);
	private Tools tools;

	private final GameTime gameTime;
	private final String username;
	private GameCamera camera = new GameCamera();

	private CardPlayerId playerID;
	private LimitedStack<GameState> states = new LimitedStack<>(30);
	private GameState currentState;

	private CardCollection collection = CardCollection.createBasicCollection();
	private CardCollection deck = CardCollection.createBasicDeck();

	private GameNetwork network = new GameNetwork();

	public NomadsGameData(GameTime gameTime, String username, List<PacketAddress> connectedPeers) {
		this.gameTime = gameTime;
		this.username = username;
		connectedPeers.forEach(network::addPeer);
	}

	@Override
	protected void init() {
		tools = new Tools(resourcePack());
		GameState state = new GameState();

		states.add(state);
		currentState = state.copy();
	}

	/**
	 * Indicates that the {@link #currentState} has finished updating, and pushes the now-finished state to the {@link
	 * #states} stack. Then, the
	 * <code>currentState</code> is replaced by a copy of the previous
	 * <code>currentState</code>.
	 */
	public void finishCurrentState() {
		GameState newCurrentState = currentState.copy();
		states.add(currentState);
		currentState = newCurrentState;
	}

	public NomadsSettings settings() {
		return settings;
	}

	public Tools tools() {
		return tools;
	}

	public GameTime gameTime() {
		return gameTime;
	}

	public long currentTimeMillis() {
		return gameTime.currentTimeMillis();
	}

	public String username() {
		return username;
	}

	public GameCamera camera() {
		return camera;
	}

	public CardPlayerId playerID() {
		return playerID;
	}

	public void setPlayerID(CardPlayerId playerID) {
		this.playerID = playerID;
	}

	public LimitedStack<GameState> states() {
		return states;
	}

	/**
	 * Gets the previous, fully updated state. The data in this state is fully updated and can be used for rendering.
	 * The previous state should NOT be mutated.
	 *
	 * @return The previous state
	 */
	public GameState previousState() {
		return states.getLast();
	}

	/**
	 * Gets the current state. The data in this state is NOT fully updated, so it should not be used for rendering. This
	 * is because some objects have been updated while the others have not. The current state gets mutated by the game
	 * logic.
	 *
	 * @return The current state
	 */
	public GameState currentState() {
		return currentState;
	}

	public CardCollection collection() {
		return collection;
	}

	public CardCollection deck() {
		return deck;
	}

	public GameNetwork network() {
		return network;
	}

}
