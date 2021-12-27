package context.game;

import static model.card.CardRarity.ARCANE;
import static model.card.CardRarity.BASIC;
import static model.card.CardType.ACTION;
import static model.card.CardType.CANTRIP;
import static model.card.effect.CardTargetType.CHARACTER;
import static model.card.effect.CardTargetType.TILE;

import common.math.Vector2f;
import context.data.GameData;
import model.GameState;
import model.actor.CardPlayer;
import model.actor.GameObject;
import model.actor.HealthActor;
import model.actor.Nomad;
import model.card.CardDashboard;
import model.card.CardType;
import model.card.GameCard;
import model.card.effect.CardEffect;
import model.card.effect.DealDamageExpression;
import model.card.effect.RegenesisExpression;
import model.card.effect.SelfDrawCardExpression;
import model.card.effect.TaskExpression;
import model.card.effect.TeleportExpression;
import model.task.MoveTask;
import model.tile.Tile;

public class NomadsGameData extends GameData {

	private CardPlayer player;
	private GameState state = new GameState();

	@Override
	protected void init() {
		Nomad n0 = new Nomad();
		n0.updatePos(new Vector2f(300, 500));
		Nomad n1 = new Nomad();
		n1.updatePos(new Vector2f(600, 300));
		state.add(n0);
		state.add(n1);
		fillDeck(n0);
		fillDeck(n1);
		player = n0;
	}

	private void fillDeck(Nomad n) {
		GameCard extraPrep = new GameCard("Extra preparation", ACTION, BASIC,
				new CardEffect(null, null, new SelfDrawCardExpression(2)), 1, "Draw 2.");
//		GameCard meteor = new GameCard("Meteor", ACTION, BASIC, new CardEffect(TILE, null, new SelfDrawCardExpression(2)), 1,
//				"Deal 8 to all characters within radius 3 of target tile.");
		GameCard move = new GameCard("Test task", CardType.TASK, BASIC,
				new CardEffect(TILE, null, new TaskExpression(() -> new MoveTask())), 0, "Move to target tile.");
		GameCard teleport = new GameCard("Teleport", CANTRIP, ARCANE,
				new CardEffect(TILE, null, new TeleportExpression()), 0, "Teleport to target tile within radius 4.");
		GameCard zap = new GameCard("Zap", CANTRIP, BASIC,
				new CardEffect(CHARACTER, (CardPlayer player, GameObject target) -> {
					if (target instanceof HealthActor) {
						HealthActor actor = (HealthActor) target;
						Vector2f relativePos = player.relativePos(actor.chunkPos(), actor.pos());
						return relativePos.multiply(1f / Tile.TILE_WIDTH, 1f / Tile.TILE_HEIGHT).lengthSquared() < 9;
					}
					return false;
				}, new DealDamageExpression(3)), 0, "Deal 3.");

		CardDashboard dashboard = n.cardDashboard();
		state.add(extraPrep);
		GameCard extraPrepCopy = extraPrep.copyDiffID();
		state.add(extraPrepCopy);
		state.add(zap);
		state.add(move);
		GameCard moveCopy = move.copyDiffID();
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
		addCopyTo(teleport, n);
		for (int i = 0; i < 4; i++) {
			addCopyTo(extraPrep, n);
		}
		dashboard.deck().shuffle(0);
		GameCard regenesis = new GameCard("Regenesis", ACTION, BASIC,
				new CardEffect(null, null, new RegenesisExpression()), 1,
				"When this card enters discard from anywhere, shuffle discard into deck.");
		state.add(regenesis);
		dashboard.deck().addBottom(regenesis);
	}

	private void addCopyTo(GameCard card, Nomad nomad) {
		GameCard copy = card.copyDiffID();
		nomad.cardDashboard().deck().addTop(copy);
		state.add(copy);
	}

	public CardPlayer player() {
		return player;
	}

	public void setPlayer(CardPlayer player) {
		this.player = player;
	}

	public GameState state() {
		return state;
	}

}
