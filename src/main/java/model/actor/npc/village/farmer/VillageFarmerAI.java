package model.actor.npc.village.farmer;

import static model.card.expression.CardTargetType.CHARACTER;
import static model.hidden.ObjectiveType.*;
import static model.item.Item.WOOD;

import java.util.List;

import common.math.Vector2i;
import event.game.logicprocessing.CardPlayedEvent;
import model.actor.Actor;
import model.actor.ItemActor;
import model.actor.NPCActor;
import model.ai.NPCActorAI;
import model.card.CardTag;
import model.card.GameCard;
import model.card.WorldCard;
import model.card.expression.CardTargetType;
import model.hidden.Objective;
import model.hidden.village.Village;
import model.item.Item;
import model.state.GameState;
import model.world.Tile;

public class VillageFarmerAI extends NPCActorAI {

	private static final int WOOD_REQUIRED_TO_BUILD_HOUSE = GameCard.BUILD_HOUSE.effect.requiredItems.get(WOOD);

	public VillageFarmerAI(Village village) {
		setObjective(VILLAGER_SURVIVE, (npc, state) -> false);
	}

	@Override
	public CardPlayedEvent playCard(NPCActor npc, GameState state) {
		while (true) {
			if (objective.isComplete(npc, state)) {
				Objective previousObjective = objective;
				Objective parent = objective.parent();
				objective = parent.subObjectives().get(parent.subObjectives().indexOf(previousObjective) + 1);
			}

			System.out.println("Trying to handle " + objective.type());
			CardTag[] tags = objective.type().tags();
			WorldCard handCard = findCardWithTag(npc.cardDashboard().hand(), tags);
			if (handCard != null) {
				System.out.println("Playing card to complete " + objective.type());
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
			objective.subObjectives().clear();
			generateSubObjectives();
			objective = objective.subObjectives().get(0);
			System.out.println("Moved down to subobjective " + objective.type());
		}
//		if (npc.cardDashboard().hand().notEmpty()) {
//			return playCard(npc.cardDashboard().hand().get(0), npc, state);
//		}
//		return null;
	}

	private WorldCard findCardWithTag(List<WorldCard> cards, CardTag... tags) {
		for (WorldCard card : cards) {
			for (CardTag objectiveTag : tags) {
				for (CardTag cardTag : card.tags()) {
					if (objectiveTag == cardTag) {
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
			Actor target = actorsAroundChunk.stream().filter(a -> card.effect().targetPredicate.test(npc, a)).findFirst().orElse(null);
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
				objective
						.addSubObjective(BUILD_HOUSE, (npc, state) -> false)
						.addSubObjective(VILLAGER_SURVIVE, (npc, state) -> false);
				break;
			case BUILD_HOUSE:
				objective
						.addSubObjective(GATHER_WOOD, (npc, state) -> npc.inventory().get(WOOD) > WOOD_REQUIRED_TO_BUILD_HOUSE)
						.addSubObjective(FIND_BUILD_HOUSE_LOCATION, (npc, state) -> false)
						.addSubObjective(ACTUALLY_BUILD_HOUSE, (npc, state) -> false);
				break;
			case GATHER_WOOD:
				objective
						.addSubObjective(CUT_TREE, VillageFarmerAI::cutWoodSuccess)
						.addSubObjective(GATHER_LOG, VillageFarmerAI::gatherWoodSuccess); // TODO add previous npc state to predicate
				break;
			default:
				throw new RuntimeException("Unhandled objective type for VillageFarmer " + objective.type());
		}
	}

	private static boolean cutWoodSuccess(NPCActor npc, GameState state) {
		List<Actor> actorsAroundChunk = state.getActorsAroundChunk(npc.worldPos().chunkPos());
		for (Actor actor : actorsAroundChunk) {
			if (actor instanceof ItemActor) {
				ItemActor itemActor = (ItemActor) actor;
				if (itemActor.item() == Item.WOOD) {
					// TODO, check distance
					return true;
				}
			}
		}
		return false;
	}

	private static boolean gatherWoodSuccess(NPCActor npc, GameState state) {
		List<Actor> actorsAroundChunk = state.getActorsAroundChunk(npc.worldPos().chunkPos());
		for (Actor actor : actorsAroundChunk) {
			if (actor instanceof ItemActor) {
				ItemActor itemActor = (ItemActor) actor;
				if (itemActor.item() == Item.WOOD) {
					// TODO, check distance
					return false;
				}
			}
		}
		// TODO check inventory to see if npc has additional wood
		return true;
	}

}
