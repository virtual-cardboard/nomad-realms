package model.actor.npc.village.chief;

import context.game.visuals.displayer.CardPlayerDisplayer;
import model.actor.NPCActor;
import model.actor.npc.village.farmer.VillageFarmerAI;
import model.hidden.village.Village;

public class VillageChief extends NPCActor {

	public VillageChief(Village village) {
		super(10);
		this.ai = new VillageChiefAI(village);
	}

	private VillageChief(long id, Village village) {
		super(10, id);
		this.ai = new VillageFarmerAI(village);
	}

	@Override
	public CardPlayerDisplayer<? extends NPCActor> displayer() {
		return null;
	}

	@Override
	public VillageChief copy() {
		return super.copyTo(new VillageChief(id, null));
	}

}
