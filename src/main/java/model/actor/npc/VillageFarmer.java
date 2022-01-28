package model.actor.npc;

import context.game.visuals.displayer.VillageFarmerDisplayer;
import model.actor.NPCActor;
import model.ai.NPCActorAI;
import model.hidden.village.Village;
import model.state.GameState;

public class VillageFarmer extends NPCActor {

	private transient VillageFarmerDisplayer displayer;

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
