package model.actor.npc.village.farmer;

import context.game.visuals.displayer.VillageFarmerDisplayer;
import model.actor.NPCActor;
import model.hidden.village.Village;

public class VillageFarmer extends NPCActor {

	private transient VillageFarmerDisplayer displayer;

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
		return super.copyTo(new VillageFarmer(id, null, displayer));
	}

}