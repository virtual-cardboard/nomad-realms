package nomadrealms.context.game.event;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.status.Status;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.item.Inventory;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.tile.factory.TileType;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.ParticlePool;
import org.junit.jupiter.api.Test;

class ProcChainTest {

	@Test
	void shouldResolveImmediatelyIfDelayIsZero() {
		World world = null;
		Tile tile = new TestTile();
		Actor actor = new TestActor(tile);

		TestEffect effect = new TestEffect(actor);
		effect.delay(0);
		ProcChain chain = new ProcChain(singletonList(effect));

		chain.update(world);

		assertTrue(effect.resolved);
		assertTrue(chain.empty());
	}

	@Test
	void shouldResolveAfterFiveTicksIfDelayIsFive() {
		World world = null;
		Tile tile = new TestTile();
		Actor actor = new TestActor(tile);

		TestEffect effect = new TestEffect(actor);
		effect.delay(5);
		ProcChain chain = new ProcChain(singletonList(effect));

		for (int i = 0; i < 4; i++) {
			chain.update(world);
			assertFalse(effect.resolved, "Effect should not be resolved at tick " + (i + 1));
		}

		chain.update(world);
		assertTrue(effect.resolved, "Effect should be resolved at tick 5");
		assertTrue(chain.empty());
	}

	private static class TestEffect extends Effect {
		boolean resolved = false;

		public TestEffect(Actor source) {
			super(source);
		}

		@Override
		public void resolve(World world) {
			resolved = true;
		}
	}

	private static class TestActor implements Actor {
		private final UUID uuid = UUID.randomUUID();
		Tile tile;

		TestActor(Tile tile) {
			this.tile = tile;
		}

		@Override
		public UUID uuid() {
			return uuid;
		}

		@Override
		public String name() {
			return null;
		}

		@Override
		public Status status() {
			return null;
		}

		@Override
		public void particlePool(ParticlePool particlePool) {
		}

		@Override
		public ParticlePool particlePool() {
			return null;
		}

		@Override
		public void render(RenderingEnvironment re) {
		}

		@Override
		public Tile tile() {
			return tile;
		}

		@Override
		public void tile(Tile tile) {
		}

		@Override
		public int health() {
			return 0;
		}

		@Override
		public void health(int health) {
		}

		@Override
		public boolean dead() {
			return false;
		}

		@Override
		public void dead(boolean dead) {
		}

		@Override
		public Inventory inventory() {
			return null;
		}

		@Override
		public Tile previousTile() {
			return null;
		}

		@Override
		public void previousTile(Tile tile) {
		}
	}

	private static class TestTile extends Tile {
		public TestTile() {
			super(null, null);
		}

		@Override
		public Chunk chunk() {
			return new TestChunk();
		}

		@Override
		public TileType type() {
			return null;
		}
	}

	private static class TestChunk extends Chunk {
		public TestChunk() {
			super(null, null);
		}

		@Override
		public List<Chunk> getSurroundingChunks() {
			return new ArrayList<>();
		}

		@Override
		public List<Actor> actors() {
			return new ArrayList<>();
		}
	}
}
