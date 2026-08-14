package nomadrealms.context.game.card;

import static nomadrealms.context.game.card.GameCard.TOTEM_OF_PAIN;
import static nomadrealms.context.game.card.GameCard.FEAR;
import static nomadrealms.context.game.card.GameCard.VOODOO_HEX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.actor.types.structure.TotemOfPainStructure;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.expression.AddCardToStackExpression;
import nomadrealms.context.game.card.query.actor.StaticTargetQuery;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.TemplateGenerationStrategy;
import nomadrealms.event.game.effect.EffectContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TotemOfPainCardTest {

	private GameState gameState;
	private Farmer source;
	private Farmer nearTarget;
	private Farmer farTarget;
	private TotemOfPainStructure totem;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(), new TemplateGenerationStrategy());

		ChunkCoordinate chunkCoord = new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0);

		// Clear tiles in 16x16 chunk
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				gameState.world.getTile(new TileCoordinate(chunkCoord, x, y)).clearActor();
			}
		}

		Tile tileSource = gameState.world.getTile(new TileCoordinate(chunkCoord, 0, 0));
		Tile tileNear = gameState.world.getTile(new TileCoordinate(chunkCoord, 1, 0));
		Tile tileFar = gameState.world.getTile(new TileCoordinate(chunkCoord, 10, 0)); // Range 10 from totem
		Tile tileTotem = gameState.world.getTile(new TileCoordinate(chunkCoord, 2, 0));

		source = new Farmer("Source", tileSource);
		nearTarget = new Farmer("NearTarget", tileNear);
		farTarget = new Farmer("FarTarget", tileFar);

		totem = new TotemOfPainStructure();
		totem.tile(tileTotem);

		gameState.world.addActor(source, true);
		gameState.world.addActor(nearTarget, true);
		gameState.world.addActor(farTarget, true);
		gameState.world.addActor(totem, true);
	}

	@Test
	public void testTotemOfPainEffectRange() {
		// Confirm distance of targets to the totem
		int distanceNearToTotem = nearTarget.tile().coord().distanceTo(totem.tile().coord());
		int distanceFarToTotem = farTarget.tile().coord().distanceTo(totem.tile().coord());

		assertTrue(distanceNearToTotem <= 6);
		assertTrue(distanceFarToTotem > 6);

		// 1. Resolve FEAR added to near target
		// We expect the modify logic to add 1 extra FEAR card, resulting in 2 FEAR cards
		List<Effect> effectsNear = AddCardToStackExpression.addCardToStack(FEAR, new StaticTargetQuery<>(nearTarget))
				.effects(new EffectContext().world(gameState.world).target(nearTarget).source(source));
		assertEquals(1, effectsNear.size());

		ProcChain chainNear = new ProcChain(effectsNear);
		chainNear.update(gameState.world);

		assertEquals(2, nearTarget.cardStack().size());
		assertEquals(FEAR, nearTarget.cardStack().get(0).card().card());
		assertEquals(FEAR, nearTarget.cardStack().get(1).card().card());

		// 2. Resolve FEAR added to far target (out of totem range)
		// We expect no modification, so exactly 1 FEAR card is added
		List<Effect> effectsFar = AddCardToStackExpression.addCardToStack(FEAR, new StaticTargetQuery<>(farTarget))
				.effects(new EffectContext().world(gameState.world).target(farTarget).source(source));
		assertEquals(1, effectsFar.size());

		ProcChain chainFar = new ProcChain(effectsFar);
		chainFar.update(gameState.world);

		assertEquals(1, farTarget.cardStack().size());
		assertEquals(FEAR, farTarget.cardStack().get(0).card().card());
	}

	@Test
	public void testTotemOfPainCardPlaysCorrectly() {
		// Test card play to spawn Totem of Pain structure
		// Empty tile for totem
		Tile targetTile = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 5, 5));
		targetTile.clearActor();

		List<Effect> effects = TOTEM_OF_PAIN.expression().effects(new EffectContext().world(gameState.world).target(targetTile).source(source));
		assertEquals(1, effects.size());

		effects.get(0).resolve(gameState.world);

		// Assert structure is spawned
		assertTrue(targetTile.actor() instanceof TotemOfPainStructure);
		assertEquals(10, targetTile.actor().health());
	}
}
