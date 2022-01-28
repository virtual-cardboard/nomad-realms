package model.hidden.village;

import java.util.List;

import model.actor.npc.VillageChief;
import model.actor.npc.VillageFarmer;
import model.hidden.village.objective.VillageObjective;

public class Village {

	public int points = 0;

	public List<VillageObjective> objectives;
	public List<VillageFarmer> farmers;
	public VillageChief chief;

}
