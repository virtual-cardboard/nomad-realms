package context.game;

import app.NomadsSettings;
import common.math.Vector2i;
import context.data.GameData;
import model.actor.Nomad;
import model.card.CardCollection;
import model.card.CardZone;
import model.id.CardPlayerID;
import model.state.GameState;
import model.state.LimitedStack;

public class NomadsGameData extends GameData {

	private CardPlayerID playerID;
	private LimitedStack<GameState> states = new LimitedStack<>(30);
	private GameState currentState;

	private NomadsSettings settings = new NomadsSettings(48f, 0.375f, 1, 1, 1);

	private CardCollection collection = CardCollection.createBasicCollection();
	private CardCollection deck = CardCollection.createBasicDeck();

	@Override
	protected void init() {
		GameState state = new GameState();
		Nomad n0 = new Nomad();
		Nomad n1 = new Nomad();
		n0.worldPos().setTilePos(new Vector2i(0, 0));
		state.add(n0);
//		fillDeck(n0, state);
		CardZone deckZone = n0.cardDashboard().deck();
		deck.addTo(deckZone, state);
		deckZone.shuffle(n0.random(-1));
		for (int i = 0; i < 6; i++) {
			n0.cardDashboard().hand().add(deckZone.drawTop());
		}
		playerID = n0.id();
//		n1.worldPos().setTilePos(new Vector2i(3, 1));
//		state.add(n1);
//		fillDeck(n1, state);

//		ItemActor wood = new ItemActor(Item.WOOD);
//		wood.worldPos().setTilePos(new Vector2i(3, 3));
//		state.add(wood);
//		{
//			VillageFarmer villageFarmer = new VillageFarmer(null);
//			WorldCard c4 = new WorldCard(GATHER);
//			WorldCard c1 = new WorldCard(CUT_TREE);
//			WorldCard c3 = new WorldCard(REGENESIS);
//			WorldCard c2 = new WorldCard(EXTRA_PREPARATION);
//			villageFarmer.cardDashboard().hand().add(c1);
//			villageFarmer.cardDashboard().hand().add(c3);
//			villageFarmer.cardDashboard().hand().add(c2);
//			villageFarmer.cardDashboard().deck().add(c4);
//			villageFarmer.worldPos().setTilePos(new Vector2i(4, 6));
//			state.add(c1);
//			state.add(c2);
//			state.add(c3);
//			state.add(c4);
//			state.add(villageFarmer);
//		}
//		{
//			VillageFarmer villageFarmer = new VillageFarmer(null);
//			playerID = villageFarmer.id();
//			WorldCard c1 = new WorldCard(CUT_TREE);
//			WorldCard c2 = new WorldCard(EXTRA_PREPARATION);
//			WorldCard c3 = new WorldCard(REGENESIS);
//			WorldCard c5 = new WorldCard(HOUSE);
//			WorldCard c4 = new WorldCard(GATHER);
//			villageFarmer.cardDashboard().hand().add(c1);
//			villageFarmer.cardDashboard().hand().add(c2);
//			villageFarmer.cardDashboard().hand().add(c3);
//			villageFarmer.cardDashboard().deck().add(c4);
//			villageFarmer.cardDashboard().hand().add(c5);
//			villageFarmer.worldPos().setChunkPos(new Vector2i(-5, -1));
//			villageFarmer.worldPos().setTilePos(new Vector2i(8, 8));
//			state.add(c1);
//			state.add(c2);
//			state.add(c3);
//			state.add(c4);
//			state.add(c5);
//			state.add(villageFarmer);
//		}
//
//		Goat goat = new Goat();
//		goat.worldPos().setChunkPos(new Vector2i(-5, -1));
//		goat.worldPos().setTilePos(new Vector2i(5, 7));
//		state.add(goat);
//
//		ItemActor itemActor = new ItemActor(Item.MEAT);
//		itemActor.worldPos().setChunkPos(new Vector2i(-5, -1));
//		itemActor.worldPos().setTilePos(new Vector2i(7, 7));
//		state.add(itemActor);

		states.add(state);
		currentState = state.copy();
	}

	public CardPlayerID playerID() {
		return playerID;
	}

	public NomadsSettings settings() {
		return settings;
	}

	/**
	 * Indicates that the {@link #currentState} has finished updating, and pushes
	 * the now-finished state to the {@link #states} stack. Then, a new
	 * <code>currentState</code> is replaced by a copy of the previous
	 * <code>currentState</code>.
	 */
	public void finishCurrentState() {
		GameState newCurrentState = currentState.copy();
		states.add(currentState);
		currentState = newCurrentState;
	}

	/**
	 * Gets the previous, fully updated state. The data in this state is fully
	 * updated and can be used for rendering. The previous state should NOT be
	 * mutated.
	 * 
	 * @return The previous state
	 */
	public GameState previousState() {
		return states.getLast();
	}

	/**
	 * Gets the current state. The data in this state is NOT fully updated, so it
	 * should not be used for rendering. This is because some objects have been
	 * updated while the others have not. The current state gets mutated by the game
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

}
