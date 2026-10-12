package nomadrealms.context.game.card.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.TemplateGenerationStrategy;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.particle.geometry.CircleParticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DashActionTest {

	private GameState gameState;
	private Nomad nomad;
	private Tile startTile;
	private Tile targetTile;
	private List<Particle> addedParticles;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(), new TemplateGenerationStrategy());
		ChunkCoordinate chunkCoord = new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0);
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				gameState.world.getTile(new TileCoordinate(chunkCoord, x, y)).clearActor();
			}
		}
		startTile = gameState.world.getTile(new TileCoordinate(chunkCoord, 0, 0));
		targetTile = gameState.world.getTile(new TileCoordinate(chunkCoord, 0, 2));
		nomad = new Nomad("Test Nomad", startTile);
		gameState.world.nomad = nomad;
		gameState.world.addActor(nomad, true);

		addedParticles = new ArrayList<>();
		ParticlePool trackingPool = new ParticlePool(null) {
			@Override
			public void addParticle(Particle particle) {
				addedParticles.add(particle);
			}
		};
		gameState.particlePool(trackingPool);
	}

	@Test
	public void testDashActionSpawnsCircleParticlesDuringUpdate() {
		DashAction dashAction = new DashAction(nomad, targetTile, 10);
		dashAction.init(gameState.world);

		assertFalse(dashAction.isComplete());
		assertEquals(0, addedParticles.size());

		while (!dashAction.isComplete()) {
			dashAction.update(gameState.world);
		}

		assertTrue(addedParticles.size() > 0);
		assertTrue(addedParticles.stream().allMatch(p -> p instanceof CircleParticle));
	}

}
