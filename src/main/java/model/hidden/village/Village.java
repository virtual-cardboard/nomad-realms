package model.hidden.village;

import java.util.List;

import model.actor.npc.VillageChief;
import model.actor.npc.VillageFarmer;
import model.hidden.HiddenGameObject;

public class Village extends HiddenGameObject {

	public int points = 0;

	public List<VillageFarmer> farmers;
	public VillageChief chief;

	@Override
	public Village copy() {
		Village village = new Village();
		village.points = points;
		return village;
	}

	@Override
	public String description() {
		return "A dingy old village with minimal education but lots of farmers.";
	}

}