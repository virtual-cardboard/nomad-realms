package model.actor.npc.village.farmer;

import context.game.data.DebugTools;
import graphics.displayer.VillageFarmerDisplayer;
import model.actor.CardPlayerSerializationFormats;
import model.actor.NpcActor;
import model.hidden.village.Village;

public class VillageFarmer extends NpcActor {

	public VillageFarmer(Village village) {
		super(10);
		this.ai = new VillageFarmerAi(village);
		setDisplayer(new VillageFarmerDisplayer());
	}

	private VillageFarmer(long id, Village village, VillageFarmerDisplayer displayer) {
		super(10, id);
		this.ai = new VillageFarmerAi(village);
		setDisplayer(displayer);
	}

	public VillageFarmer(Village village, VillageFarmerDisplayer displayer, DebugTools tools) {
		super(10);
		this.ai = new VillageFarmerAi(village, tools);
		setDisplayer(displayer);
	}

	@Override
	public VillageFarmer copy() {
		return super.copyTo(new VillageFarmer(id, null, (VillageFarmerDisplayer) displayer()));
	}

	@Override
	public CardPlayerSerializationFormats formatEnum() {
		return null; // TODO
	}

}
