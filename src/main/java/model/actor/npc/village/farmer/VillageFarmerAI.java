package model.actor.npc.village.farmer;

import static model.card.expression.CardTargetType.CHARACTER;
import static model.hidden.ObjectiveType.*;

import java.util.List;

import event.game.logicprocessing.CardPlayedEvent;
import model.actor.Actor;
import model.actor.NPCActor;
import model.ai.NPCActorAI;
import model.card.CardZone;
import model.card.WorldCard;
import model.hidden.village.Village;
import model.state.GameState;

public class VillageFarmerAI extends NPCActorAI {

	public VillageFarmerAI(Village village) {
		setObjective(VILLAGER_SURVIVE);
	}

	@Override
	public CardPlayedEvent playCard(NPCActor npc, GameState state) {
		CardZone hand = npc.cardDashboard().hand();
		if (!hand.isEmpty()) {
			WorldCard card = hand.get(0);
			if (card.effect().targetType == null) {
				return new CardPlayedEvent(npc.id(), card.id(), 0);
			} else if (card.effect().targetType == CHARACTER) {
				List<Actor> actorsAroundChunk = state.getActorsAroundChunk(npc.worldPos().chunkPos());
				Actor target = actorsAroundChunk.stream().filter(a -> card.effect().condition.test(npc, a)).findFirst().orElse(null);
				if (target != null) {
					return new CardPlayedEvent(npc.id(), card.id(), target.id());
				}
			}
		}
		return null;
	}

	@Override
	public VillageFarmerAI copy() {
		VillageFarmerAI copy = new VillageFarmerAI(null);
		return super.copyTo(copy);
	}

	@Override
	public int genTickDelay() {
		return 10 + (int) (Math.random() * 10);
	}

	@Override
	public void generateSubObjectives() {
		switch (objective.type()) {
			case VILLAGER_SURVIVE:
				objective.setSubObjectives(BUILD_HOUSE, VILLAGER_SURVIVE);
			case BUILD_HOUSE:
				objective.setSubObjectives(GATHER_WOOD, FIND_BUILD_HOUSE_LOCATION, ACTUALLY_BUILD_HOUSE);
			case GATHER_WOOD:
				objective.setSubObjectives(FIND_TREE, CUT_TREE, GATHER_LOG);
			default:
				throw new RuntimeException("Unhandled objective type for VillageFarmer");
		}
	}

}
