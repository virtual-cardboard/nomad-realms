package context.game;

import static model.card.GameCard.EXTRA_PREPARATION;
import static model.card.GameCard.INTERACT;
import static model.card.GameCard.MOVE;
import static model.card.GameCard.PLANNING_TABLE;
import static model.card.GameCard.REGENESIS;

import app.NomadsSettings;
import common.math.Vector2i;
import context.data.GameData;
import model.actor.Nomad;
import model.card.CardCollection;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.id.CardPlayerID;
import model.state.GameState;
import model.state.LimitedStack;

public class NomadsGameData extends GameData {

	private CardPlayerID playerID;
	private LimitedStack<GameState> states = new LimitedStack<>(30);
	private GameState currentState;

	private NomadsSettings settings = new NomadsSettings(48f, 0.375f, 1, 1, 1);

	private CardCollection collection = new CardCollection();

	@Override
	protected void init() {
		GameState state = new GameState();
		Nomad n0 = new Nomad();
		n0.worldPos().setTilePos(new Vector2i(0, 0));
		state.add(n0);
		fillDeck(n0, state);
		Nomad n1 = new Nomad();
		n1.worldPos().setTilePos(new Vector2i(2, 1));
		state.add(n1);
		fillDeck(n1, state);
		playerID = n0.id();

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

	private void fillDeck(Nomad n, GameState state) {
		WorldCard extraPrep = new WorldCard(EXTRA_PREPARATION);
		WorldCard move = new WorldCard(MOVE);
		WorldCard planningTable = new WorldCard(PLANNING_TABLE);
		WorldCard interact = new WorldCard(INTERACT);

		CardDashboard dashboard = n.cardDashboard();
		state.add(extraPrep);
		WorldCard extraPrepCopy = extraPrep.copyDiffID();
		state.add(extraPrepCopy);
		state.add(move);
		state.add(interact);
		state.add(planningTable);
		dashboard.hand().addTop(extraPrep);
		dashboard.hand().addTop(extraPrepCopy);
		dashboard.hand().addTop(move);
		dashboard.hand().addTop(interact);
		dashboard.hand().addTop(planningTable);
		for (int i = 0; i < 4; i++) {
			addCopyTo(extraPrep, n, state);
		}
		dashboard.deck().shuffle(n.random(-1));
		WorldCard regenesis = new WorldCard(REGENESIS);
		state.add(regenesis);
		dashboard.deck().addBottom(regenesis);
	}

	private void addCopyTo(WorldCard card, Nomad nomad, GameState state) {
		WorldCard copy = card.copyDiffID();
		nomad.cardDashboard().deck().addTop(copy);
		state.add(copy);
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

}
