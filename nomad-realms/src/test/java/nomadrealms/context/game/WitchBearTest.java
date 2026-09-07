package nomadrealms.context.game;

import static nomadrealms.context.game.card.GameCard.DEBILITATING_FEAR;
import static nomadrealms.context.game.card.GameCard.FEAR;
import static nomadrealms.context.game.card.GameCard.TOTEM_OF_PAIN;
import static nomadrealms.context.game.card.GameCard.VOODOO_HEX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.cardplayer.WitchBear;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.actor.types.structure.TotemOfPainStructure;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.TemplateGenerationStrategy;
import org.junit.jupiter.api.Test;

public class WitchBearTest {

	@Test
	public void testWitchBearInitializationAndAi() {
		GameState gameState = new GameState("Test World", new LinkedList<>(), new TemplateGenerationStrategy());
		World world = gameState.world;

		TileCoordinate centerCoord = new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 5, 5);
		WitchBear witchBear = new WitchBear("Test Witch Bear", world.getTile(centerCoord));
		Nomad nomad = new Nomad("Test Nomad", world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 5, 6)));
		world.nomad = nomad;

		world.addActor(witchBear, true);
		world.addActor(nomad, true);

		assertEquals(20, witchBear.health());
		assertEquals("Test Witch Bear", witchBear.name());

		// AI step 1: No Totem nearby, should queue TOTEM_OF_PAIN
		for (int i = 0; i < 60; i++) {
			world.update(null);
			if (!witchBear.lastPlays().isEmpty()) {
				break;
			}
		}
		assertEquals(1, witchBear.lastPlays().size());
		assertEquals(TOTEM_OF_PAIN, ((CardPlayedEvent) witchBear.lastPlays().get(0)).card().card());

		// Resolve plays / update world to place totem
		for (int i = 0; i < 50; i++) {
			world.update(null);
		}

		// Verify totem structure exists on a neighboring tile
		boolean totemPlaced = world.getTile(centerCoord).dl(world).actor() instanceof TotemOfPainStructure
				|| world.getTile(centerCoord).dm(world).actor() instanceof TotemOfPainStructure
				|| world.getTile(centerCoord).dr(world).actor() instanceof TotemOfPainStructure
				|| world.getTile(centerCoord).ul(world).actor() instanceof TotemOfPainStructure
				|| world.getTile(centerCoord).um(world).actor() instanceof TotemOfPainStructure
				|| world.getTile(centerCoord).ur(world).actor() instanceof TotemOfPainStructure;
		assertTrue(totemPlaced, "Witch Bear should have placed Totem of Pain");

		// AI step 2: Target Nomad within range 3, should queue VOODOO_HEX
		for (int i = 0; i < 60; i++) {
			world.update(null);
			if (witchBear.lastPlays().size() >= 2) {
				break;
			}
		}
		assertEquals(2, witchBear.lastPlays().size());
		assertEquals(VOODOO_HEX, ((CardPlayedEvent) witchBear.lastPlays().get(1)).card().card());

		// Resolve Voodoo Hex play until Nomad receives Fear card in stack
		for (int i = 0; i < 100; i++) {
			world.update(null);
			if (nomad.cardStack().getCards().stream().anyMatch(entry -> entry.event().card().card() == FEAR)) {
				break;
			}
		}

		// Nomad should now have Fear card(s) added to stack
		boolean nomadHasFear = nomad.cardStack().getCards().stream().anyMatch(entry -> entry.event().card().card() == FEAR);
		assertTrue(nomadHasFear, "Nomad should have received Fear card in stack from Voodoo Hex");

		// AI step 3: Nomad has Fear in stack, Witch Bear should queue DEBILITATING_FEAR
		for (int i = 0; i < 100; i++) {
			world.update(null);
			if (witchBear.lastPlays().size() >= 3) {
				break;
			}
		}
		assertEquals(3, witchBear.lastPlays().size());
		assertEquals(DEBILITATING_FEAR, ((CardPlayedEvent) witchBear.lastPlays().get(2)).card().card());
	}

}
