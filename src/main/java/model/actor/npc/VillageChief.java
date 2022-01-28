package model.actor.npc;

import context.game.visuals.displayer.CardPlayerDisplayer;
import model.actor.CardPlayer;
import model.actor.NPCActor;
import model.ai.NPCActorAI;
import model.hidden.village.Village;
import model.state.GameState;

public class VillageChief extends NPCActor {

	private static class VillageChiefAI implements NPCActorAI {
		public VillageChiefAI(Village village) {
		}

		@Override
		public void update(NPCActor npc, GameState state) {

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
