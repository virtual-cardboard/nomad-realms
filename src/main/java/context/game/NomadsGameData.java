package context.game;

import static app.NomadsSettings.DEFAULT_SETTINGS;
import static model.card.CardRarity.ARCANE;
import static model.card.CardRarity.BASIC;
import static model.card.CardType.ACTION;
import static model.card.CardType.CANTRIP;
import static model.card.expression.CardTargetType.CHARACTER;
import static model.card.expression.CardTargetType.TILE;

import app.NomadsSettings;
import common.math.Vector2f;
import context.data.GameData;
import model.actor.Nomad;
import model.card.CardDashboard;
import model.card.CardEffect;
import model.card.CardType;
import model.card.WorldCard;
import model.card.condition.RangeCondition;
import model.card.expression.DealDamageExpression;
import model.card.expression.RegenesisExpression;
import model.card.expression.SelfDrawCardExpression;
import model.card.expression.TaskExpression;
import model.card.expression.TeleportExpression;
import model.state.GameState;
import model.state.LimitedStack;
import model.task.MoveTask;

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
		Nomad n1 = new Nomad();
		n1.updatePos(new Vector2f(9000, 3000));
		state.add(n0);
		state.add(n1);
		fillDeck(n0, state);
		fillDeck(n1, state);
		playerID = n1.id();
		states.add(state);
		nextState = state.copy();
	}

	private void fillDeck(Nomad n, GameState state) {
		WorldCard extraPrep = new WorldCard("Extra preparation", ACTION, BASIC,
				new CardEffect(null, null, new SelfDrawCardExpression(2)), 1, "Draw 2.");
//		GameCard meteor = new GameCard("Meteor", ACTION, BASIC, new CardEffect(TILE, null, new SelfDrawCardExpression(2)), 1,
//				"Deal 8 to all characters within radius 3 of target tile.");
		WorldCard move = new WorldCard("Test task", CardType.TASK, BASIC,
				new CardEffect(TILE, null, new TaskExpression(() -> new MoveTask())), 0, "Move to target tile.");
		WorldCard teleport = new WorldCard("Teleport", CANTRIP, ARCANE,
				new CardEffect(TILE, null, new TeleportExpression()), 0, "Teleport to target tile within radius 4.");
		WorldCard zap = new WorldCard("Zap", CANTRIP, BASIC,
				new CardEffect(CHARACTER, new RangeCondition(4), new DealDamageExpression(3)), 0, "Deal 3 to target character within range 4.");

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
		WorldCard regenesis = new WorldCard("Regenesis", ACTION, BASIC,
				new CardEffect(null, null, new RegenesisExpression()), 1,
				"When this card enters discard from anywhere, shuffle discard into deck.");
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
