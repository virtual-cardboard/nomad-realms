package model.actor;

import model.ai.NPCActorAI;
import model.hidden.Village;
import model.state.GameState;

public class VillageFarmer extends NPCActor {

	private static class VillageFarmerAI implements NPCActorAI {
		public VillageFarmerAI(Village village) {
		}

		@Override
		public void update(NPCActor npc, GameState state) {

		}

		@Override
		public NPCActorAI copy() {
			return null;
		}
	}

	public VillageFarmer(Village village) {
		super(10);
		this.ai = new VillageFarmerAI(village);
	}

}
