package context.game;

import static app.NomadsSettings.DEFAULT_SETTINGS;
import static model.card.GameCard.EXTRA_PREPARATION;
import static model.card.GameCard.MOVE;
import static model.card.GameCard.REGENESIS;
import static model.card.GameCard.TELEPORT;
import static model.card.GameCard.ZAP;

import app.NomadsSettings;
import common.math.Vector2f;
import context.data.GameData;
import model.actor.Nomad;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.state.GameState;
import model.state.LimitedStack;

public class NomadsGameData extends GameData {

	private long playerID;
	private LimitedStack<GameState> states = new LimitedStack<>(30);
	private GameState nextState;

	private NomadsSettings settings = DEFAULT_SETTINGS;

	@Override
	protected void init() {
		GameState state = new GameState();
		states.add(state);
		Nomad n0 = new Nomad();
		n0.updatePos(new Vector2f(500, 0));
		state.add(n0);
		fillDeck(n0, state);
		Nomad n1 = new Nomad();
		n1.updatePos(new Vector2f(9000, 3000));
		state.add(n1);
		fillDeck(n1, state);
		playerID = n1.id();
		states.add(state);
		nextState = state.copy();
	}

	private void fillDeck(Nomad n, GameState state) {
		WorldCard extraPrep = new WorldCard(EXTRA_PREPARATION);
		WorldCard move = new WorldCard(MOVE);
		WorldCard teleport = new WorldCard(TELEPORT);
		WorldCard zap = new WorldCard(ZAP);

		CardDashboard dashboard = n.cardDashboard();
		state.add(extraPrep);
		WorldCard extraPrepCopy = extraPrep.copyDiffID();
		state.add(extraPrepCopy);
		state.add(zap);
		state.add(move);
		WorldCard moveCopy = move.copyDiffID();
		state.add(moveCopy);
		state.add(teleport);
		dashboard.hand().addTop(extraPrep);
		dashboard.hand().addTop(extraPrepCopy);
		dashboard.hand().addTop(zap);
		dashboard.hand().addTop(move);
		dashboard.hand().addTop(moveCopy);
		dashboard.hand().addTop(teleport);
//		for (int i = 0; i < 2; i++) {
//			addCopyTo(zap, n);
//		}
		addCopyTo(teleport, n, state);
		for (int i = 0; i < 4; i++) {
			addCopyTo(extraPrep, n, state);
		}
		dashboard.deck().shuffle(0);
		WorldCard regenesis = new WorldCard(REGENESIS);
		state.add(regenesis);
		dashboard.deck().addBottom(regenesis);
	}

	private void addCopyTo(WorldCard card, Nomad nomad, GameState state) {
		WorldCard copy = card.copyDiffID();
		nomad.cardDashboard().deck().addTop(copy);
		state.add(copy);
	}

	public long playerID() {
		return playerID;
	}

	public void setPlayerID(long playerID) {
		this.playerID = playerID;
	}

	public NomadsSettings settings() {
		return settings;
	}

	public LimitedStack<GameState> states() {
		return states;
	}

	public GameState nextState() {
		return nextState;
	}

	public void setNextState(GameState nextState) {
		this.nextState = nextState;
	}

}
