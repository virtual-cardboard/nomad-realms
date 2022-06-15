package context.game;

import app.NomadsSettings;
import context.data.GameData;
import context.game.visuals.GameCamera;
import debugui.ConsoleGui;
import debugui.RollingAverageStat;
import engine.common.time.GameTime;
import model.card.CardCollection;
import model.id.CardPlayerID;
import model.state.GameState;
import model.state.LimitedStack;

public class NomadsGameData extends GameData {

	private NomadsSettings settings = new NomadsSettings(48f, 0.375f, 1, 1, 1);

	private final GameTime gameTime;
	private final String username;
	private GameCamera camera = new GameCamera();

	private CardPlayerID playerID;
	private LimitedStack<GameState> states = new LimitedStack<>(30);
	private GameState currentState;

	private CardCollection collection = CardCollection.createBasicCollection();
	private CardCollection deck = CardCollection.createBasicDeck();

	private RollingAverageStat rollingAverageStat;
	private ConsoleGui consoleGui;

	public NomadsGameData(GameTime gameTime, String username) {
		this.gameTime = gameTime;
		this.username = username;
	}

	@Override
	protected void init() {
		rollingAverageStat = new RollingAverageStat(10, resourcePack());
		consoleGui = new ConsoleGui(resourcePack());

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

	public void logMessage(String message, int messageColor) {
		consoleGui.log(message, messageColor);
	}

	public NomadsSettings settings() {
		return settings;
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

	public CardPlayerID playerID() {
		return playerID;
	}

	public void setPlayerID(CardPlayerID playerID) {
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

	public RollingAverageStat rollingAverageStat() {
		return rollingAverageStat;
	}

	public ConsoleGui consoleGui() {
		return consoleGui;
	}

}
