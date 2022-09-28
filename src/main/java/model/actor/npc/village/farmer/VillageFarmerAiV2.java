//package model.actor.npc.village.farmer;
//
//import static model.card.expression.CardTargetType.CHARACTER;
//import static model.hidden.objective.ObjectiveType.ACTUALLY_BUILD_HOUSE;
//import static model.hidden.objective.ObjectiveType.BUILD_HOUSE;
//import static model.hidden.objective.ObjectiveType.CUT_TREE;
//import static model.hidden.objective.ObjectiveType.FIND_BUILD_HOUSE_LOCATION;
//import static model.hidden.objective.ObjectiveType.GATHER_LOG;
//import static model.hidden.objective.ObjectiveType.GATHER_WOOD;
//import static model.hidden.objective.ObjectiveType.VILLAGER_SURVIVE;
//import static model.item.Item.WOOD;
//import static util.UtilAlways.always;
//
//import java.util.List;
//
//import context.game.data.DebugTools;
//import engine.common.math.Vector2i;
//import event.logicprocessing.CardPlayedEvent;
//import math.WorldPos;
//import model.actor.Actor;
//import model.actor.ItemActor;
//import model.actor.health.cardplayer.NpcActor;
//import model.ai.NpcActorAiV2;
//import model.card.CardTag;
//import model.card.GameCard;
//import model.card.WorldCard;
//import model.card.expression.CardTargetType;
//import model.hidden.memory.GameMemory;
//import model.hidden.objective.Objective;
//import model.hidden.objective.decomposition.ObjectiveDecompositionRule;
//import model.hidden.village.Village;
//import model.item.Item;
//import model.state.GameState;
//import model.world.tile.Tile;
//import util.UtilAlways;
//
//public class VillageFarmerAiV2 extends NpcActorAiV2 {
//
//	private int numHouses = 0;
//
//	private static final int WOOD_REQUIRED_TO_BUILD_HOUSE = GameCard.HOUSE.effect.requiredItems.get(WOOD);
//	private static final ObjectiveDecompositionRule SURVIVE_DECOMPOSITION =
//			always().decompose(VILLAGER_SURVIVE)
//					.into(
//							(parent, npc, state) -> new Objective(BUILD_HOUSE, parent, (n,s)->true)
//	);
//	private static final int WOOD_REQUIRED_TO_BUILD_HOUSE = GameCard.HOUSE.effect.requiredItems.get(WOOD);
//	private static final int WOOD_REQUIRED_TO_BUILD_HOUSE = GameCard.HOUSE.effect.requiredItems.get(WOOD);
//
//	private GameMemory<WorldPos> locationMemory;
//
//	private DebugTools tools;
//
//	public VillageFarmerAiV2(Village village) {
//super(VILLAGER_SURVIVE, new ObjectiveDecompositionRule())
//		setObjective(VILLAGER_SURVIVE, (npc, state) -> false);
//	}
//
//	public VillageFarmerAiV2(Village village, DebugTools tools) {
//		this(village);
//		this.tools = tools;
//	}
//
//	@Override
//	public CardPlayedEvent playCard(NpcActor npc, GameState state) {
//		while (true) {
//			while (objective.isComplete(npc, state)) {
//				tools.consoleGui.log(objective.type() + " completed");
//				Objective previousObjective = objective;
//				Objective parent = objective.parent();
//				int indexOf = parent.subObjQectives().indexOf(previousObjective);
//				if (indexOf + 1 == parent.subObjectives().size()) {
//					if (parent.isComplete(npc, state)) {
//						objective = parent;
//						tools.consoleGui.log("Ascending to " + objective.type());
//						continue;
//					} else {
//						throw new RuntimeException("Unknown state, check VillageFarmerAI");
//					}
//				}
//				objective = parent.subObjectives().get(indexOf + 1);
//				tools.consoleGui.log("\tShifting to subobjective " + objective.type());
//			}
//			CardTag[] tags = objective.type().tags();
//			WorldCard handCard = findCardWithTag(npc.cardDashboard().hand(), tags);
//			if (handCard != null) {
//				tools.consoleGui.log("Playing card to complete " + objective.type());
//				return playCard(handCard, npc, state);
//			}
//			WorldCard queueCard = findCardWithTag(npc.cardDashboard().queue().toCardZone(state), tags);
//			if (queueCard != null) {
//				// Card in queue so we wait
//				tools.consoleGui.log("Card in queue, doing nothing");
//				return null;
//			}
//			WorldCard deckCard = findCardWithTag(npc.cardDashboard().deck(), tags);
//			if (deckCard != null) {
//				// Card in deck so we try to draw
//				tools.consoleGui.log("Card in deck");
//				WorldCard drawCard = findCardWithTag(npc.cardDashboard().hand(), CardTag.SELF_DRAW, CardTag.DRAW);
//				if (drawCard != null) {
//					tools.consoleGui.log("Playing " + drawCard.name() + " to try to draw " + deckCard.name());
//					return playCard(drawCard, npc, state);
//				}
//				tools.consoleGui.log("No card draw");
//				return null;
//			}
//			// Otherwise, objective cannot be completed
//			tools.consoleGui.log("Decomposing current objective: " + objective.type());
//			objective.subObjectives().clear();
//			generateSubObjectives();
//			for (Objective o : objective.subObjectives()) {
//				tools.consoleGui.log("\tSubobjective: " + o.type());
//			}
//			objective = objective.subObjectives().get(0);
//			tools.consoleGui.log("    Descending to subobjective " + objective.type());
//		}
////		if (npc.cardDashboard().hand().notEmpty()) {
////			return playCard(npc.cardDashboard().hand().get(0), npc, state);
////		}
////		return null;
//	}
//
//	private WorldCard findCardWithTag(List<WorldCard> cards, CardTag... tags) {
//		for (WorldCard card : cards) {
//			for (CardTag objectiveTag : tags) {
//				for (CardTag cardTag : card.tags()) {
//					if (objectiveTag == cardTag) {
//						return card;
//					}
//				}
//			}
//		}
//		return null;
//	}
//
//	private CardPlayedEvent playCard(WorldCard card, NpcActor npc, GameState state) {
//		if (card.effect().targetType == null) {
//			return new CardPlayedEvent(npc.id(), null, card.id());
//		} else if (card.effect().targetType == CHARACTER) {
//			List<Actor> actorsAroundChunk = state.getActorsAroundChunk(npc.worldPos().chunkPos());
//			Actor target = actorsAroundChunk.stream().filter(a -> card.effect().targetPredicate.test(npc, a)).findFirst().orElse(null);
//			if (target != null) {
//				return new CardPlayedEvent(npc.id(), target.id(), card.id());
//			}
//		} else if (card.effect().targetType == CardTargetType.TILE) {
//			Tile tile = state.worldMap().tile(npc.worldPos().copy().add(new Vector2i(0, -5)));
//			return new CardPlayedEvent(npc.id(), tile.id(), card.id());
//		}
//		return null;
//	}
//
//	@Override
//	public VillageFarmerAiV2 copy() {
//		VillageFarmerAiV2 copy = new VillageFarmerAiV2(null);
//		return super.copyTo(copy);
//	}
//
//	@Override
//	public int genTickDelay(NpcActor npc, long tick) {
//		return 10 + npc.getRandom(tick).nextInt(10);
//	}
//
//	@Override
//	public void generateSubObjectives() {
//		switch (objective.type()) {
//			case VILLAGER_SURVIVE:
//				objective
//						.addSubObjective(BUILD_HOUSE, (npc, state) -> false)
//						.addSubObjective(VILLAGER_SURVIVE, (npc, state) -> false);
//				break;
//			case BUILD_HOUSE:
//				objective
//						.addSubObjective(GATHER_WOOD, (npc, state) -> npc.inventory().get(WOOD) >= WOOD_REQUIRED_TO_BUILD_HOUSE)
//						.addSubObjective(FIND_BUILD_HOUSE_LOCATION, (npc, state) -> false)
//						.addSubObjective(ACTUALLY_BUILD_HOUSE, (npc, state) -> false);
//				break;
//			case GATHER_WOOD:
//				objective
//						.addSubObjective(CUT_TREE, VillageFarmerAiV2::cutWoodSuccess)
//						.addSubObjective(GATHER_LOG, VillageFarmerAiV2::gatherWoodSuccess); // TODO add previous npc state to predicate
//				break;
//			default:
//				throw new RuntimeException("Unhandled objective type for VillageFarmer " + objective.type());
//		}
//	}
//
//	private static boolean cutWoodSuccess(NpcActor npc, GameState state) {
//		List<Actor> actorsAroundChunk = state.getActorsAroundChunk(npc.worldPos().chunkPos());
//		for (Actor actor : actorsAroundChunk) {
//			if (actor instanceof ItemActor) {
//				ItemActor itemActor = (ItemActor) actor;
//				if (itemActor.item() == Item.WOOD) {
//					// TODO, check distance
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//
//	private static boolean gatherWoodSuccess(NpcActor npc, GameState state) {
//		List<Actor> actorsAroundChunk = state.getActorsAroundChunk(npc.worldPos().chunkPos());
//		for (Actor actor : actorsAroundChunk) {
//			if (actor instanceof ItemActor) {
//				ItemActor itemActor = (ItemActor) actor;
//				if (itemActor.item() == Item.WOOD) {
//					// TODO, check distance
//					return false;
//				}
//			}
//		}
//		// TODO check inventory to see if npc has additional wood
//		return true;
//	}
//
//}
