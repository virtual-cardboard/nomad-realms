package model.actor.npc;

import static model.card.expression.CardTargetType.CHARACTER;

import java.util.List;
import java.util.Queue;

import context.game.visuals.displayer.VillageFarmerDisplayer;
import event.game.logicprocessing.CardPlayedEvent;
import model.actor.Actor;
import model.actor.NPCActor;
import model.ai.NPCActorAI;
import model.card.CardZone;
import model.card.WorldCard;
import model.hidden.village.Village;
import model.state.GameState;

public class VillageFarmer extends NPCActor {

	private transient VillageFarmerDisplayer displayer;

	private static class VillageFarmerAI extends NPCActorAI {

		private int timer = 10;

		public VillageFarmerAI(Village village) {
		}

		@Override
		public void update(NPCActor npc, GameState state, Queue<CardPlayedEvent> queue) {
			if (timer != 0) {
				timer--;
				return;
			}
			timer = 10;
			CardZone hand = npc.cardDashboard().hand();
			if (!hand.isEmpty()) {
				WorldCard card = hand.get(0);
				if (card.effect().targetType == null) {
					queue.add(new CardPlayedEvent(npc.id(), card.id(), 0));
				} else if (card.effect().targetType == CHARACTER) {
					List<Actor> actorsAroundChunk = state.getActorsAroundChunk(npc.worldPos().chunkPos());
					Actor target = actorsAroundChunk.stream().filter(a -> card.effect().condition.test(npc, a)).findFirst().orElse(null);
					if (target != null) {
						queue.add(new CardPlayedEvent(npc.id(), card.id(), target.id()));
					}
				}
			}

		}

		@Override
		public NPCActorAI copy() {
			return null;
		}
	}

	public VillageFarmer(Village village) {
		super(10);
		this.ai = new VillageFarmerAI(village);
		displayer = new VillageFarmerDisplayer(id);
	}

	private VillageFarmer(long id, Village village, VillageFarmerDisplayer displayer) {
		super(10, id);
		this.ai = new VillageFarmerAI(village);
		this.displayer = displayer;
	}

	@Override
	public VillageFarmerDisplayer displayer() {
		return displayer;
	}

	@Override
	public VillageFarmer copy() {
		// TODO
		return super.copyTo(new VillageFarmer(id, null, displayer));
	}

}
