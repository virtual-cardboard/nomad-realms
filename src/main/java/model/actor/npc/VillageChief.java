package model.actor.npc;

import java.util.Queue;

import context.game.visuals.displayer.CardPlayerDisplayer;
import event.game.logicprocessing.CardPlayedEvent;
import model.actor.CardPlayer;
import model.actor.NPCActor;
import model.ai.NPCActorAI;
import model.ai.VillageChiefObjective;
import model.hidden.village.Village;
import model.state.GameState;

public class VillageChief extends NPCActor {

	private static class VillageChiefAI extends NPCActorAI {
		private Village village;

		public VillageChiefAI(Village village) {
			this.village = village;
			setObjective(new VillageChiefObjective());
		}

		@Override
		public void update(NPCActor npc, GameState state, Queue<CardPlayedEvent> queue) {

		}

		@Override
		public NPCActorAI copy() {
			return null;
		}
	}

	public VillageChief(Village village) {
		super(10);
		this.ai = new VillageChiefAI(village);
	}

	@Override
	public CardPlayerDisplayer<? extends NPCActor> displayer() {
		return null;
	}

	@Override
	public CardPlayer copy() {
		return null;
	}

}
