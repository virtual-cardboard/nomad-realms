package model.actor.npc.village.farmer;

import static model.card.expression.CardTargetType.CHARACTER;
import static model.hidden.ObjectiveType.*;

import java.util.List;

import common.math.Vector2i;
import event.game.logicprocessing.CardPlayedEvent;
import model.actor.Actor;
import model.actor.NPCActor;
import model.ai.NPCActorAI;
import model.card.CardTag;
import model.card.WorldCard;
import model.card.expression.CardTargetType;
import model.hidden.village.Village;
import model.state.GameState;
import model.world.Tile;

public class VillageFarmerAI extends NPCActorAI {

	public VillageFarmerAI(Village village) {
		setObjective(VILLAGER_SURVIVE);
	}

	@Override
	public CardPlayedEvent playCard(NPCActor npc, GameState state) {
		while (true) {
			CardTag[] tags = objective.type().tags();
			WorldCard handCard = findCardWithTag(npc.cardDashboard().hand(), tags);
			if (handCard != null) {
				return playCard(handCard, npc, state);
			}
			WorldCard queueCard = findCardWithTag(npc.cardDashboard().queue().toCardZone(state), tags);
			WorldCard deckCard = findCardWithTag(npc.cardDashboard().deck(), tags);
			if (queueCard != null || deckCard != null) {
				// Card not in hand so we wait
				System.out.println("Card in queue or deck");
				return null;
			}
			// Otherwise, objective cannot be completed
			generateSubObjectives();
			objective = objective.subObjectives().get(0);
			System.out.println(objective.type());
		}
	}

	private WorldCard findCardWithTag(List<WorldCard> cards, CardTag... tags) {
		for (WorldCard card : cards) {
			for (CardTag objectiveTag : tags) {
				for (CardTag cardTag : card.tags()) {
					if (objectiveTag == cardTag) {
						objective = objective.parent();
						return card;
					}
				}
			}
		}
		return null;
	}

	private CardPlayedEvent playCard(WorldCard card, NPCActor npc, GameState state) {
		if (card.effect().targetType == null) {
			return new CardPlayedEvent(npc.id(), card.id(), 0);
		} else if (card.effect().targetType == CHARACTER) {
			List<Actor> actorsAroundChunk = state.getActorsAroundChunk(npc.worldPos().chunkPos());
			Actor target = actorsAroundChunk.stream().filter(a -> card.effect().condition.test(npc, a)).findFirst().orElse(null);
			if (target != null) {
				return new CardPlayedEvent(npc.id(), card.id(), target.id());
			}
		} else if (card.effect().targetType == CardTargetType.TILE) {
			Tile tile = state.worldMap().tile(npc.worldPos().copy().add(new Vector2i(0, -5)));
			return new CardPlayedEvent(npc.id(), card.id(), tile.id());
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
				break;
			case BUILD_HOUSE:
				objective.setSubObjectives(GATHER_WOOD, FIND_BUILD_HOUSE_LOCATION, ACTUALLY_BUILD_HOUSE);
				break;
			case GATHER_WOOD:
				objective.setSubObjectives(CUT_TREE, GATHER_LOG);
				break;
			default:
				throw new RuntimeException("Unhandled objective type for VillageFarmer " + objective.type());
		}
	}

}
