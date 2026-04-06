package nomadrealms.context.game.card;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.actor.types.cardplayer.creature.Creature;
import nomadrealms.context.game.actor.types.cardplayer.creature.Spiderling;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.TemplateGenerationStrategy;
import nomadrealms.event.game.effect.EffectContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static nomadrealms.context.game.card.GameCard.SPIDERLING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpiderlingCardTest {

	private GameState gameState;
	private Farmer source;
	private Tile targetTile;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(),
				new TemplateGenerationStrategy());
		Tile tile1 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		targetTile = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 1));
		source = new Farmer("Source", tile1);
		gameState.world.addActor(source, true);
	}

	@Test
	public void testSpiderlingSummon() {
		WorldCard spiderlingCard = new WorldCard(source.deckCollection().deck1(), SPIDERLING);
		Effect summonEffect = SPIDERLING.expression().effects(new EffectContext().world(gameState.world).source(source).target(targetTile).card(spiderlingCard)).get(0);
		summonEffect.resolve(gameState.world);

		assertTrue(targetTile.actor() instanceof Spiderling);
		Creature spiderling = (Creature) targetTile.actor();
		assertEquals(3, spiderling.health());
		assertEquals(10, spiderling.mana());
		assertEquals(2, spiderling.deckCollection().deck1().size());
		assertEquals(GameCard.MOVE, spiderling.deckCollection().deck1().getCards().get(0).card());
		assertEquals(GameCard.CREATE_ROCK, spiderling.deckCollection().deck1().getCards().get(1).card());
	}
}
