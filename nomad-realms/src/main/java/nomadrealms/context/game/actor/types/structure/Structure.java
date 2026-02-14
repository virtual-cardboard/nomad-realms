package nomadrealms.context.game.actor.types.structure;

import static java.util.Collections.emptyList;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import java.util.ArrayList;
import java.util.List;

import engine.common.math.Vector2f;
import engine.serialization.Derializable;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.status.Status;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.card.action.scheduler.ActionScheduler;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.trigger.Trigger;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.item.Inventory;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.NullParticlePool;
import nomadrealms.render.particle.ParticlePool;

@Derializable
public class Structure implements Actor {

	private transient ParticlePool particlePool = new NullParticlePool();
	private final ActionScheduler actionScheduler = new ActionScheduler();
	private List<Trigger> triggers = new ArrayList<>();

	private TileCoordinate tileCoord;
	private transient Tile tile;
	private Inventory inventory;
	private final Status status = new Status();

	private String name;
	protected final String image;
	private final int constructionTime;
	private final int maxHealth;
	private int health;

	protected Structure() {
		this.name = null;
		this.image = null;
		this.constructionTime = 0;
		this.maxHealth = 0;
	}

	public Structure(String name, String image, int constructionTime, int health) {
		this.name = name;
		this.image = image;
		this.constructionTime = constructionTime;
		this.maxHealth = health;
		this.health = health;
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS * re.camera.zoom().get();
		Vector2f screenPosition = tile().getScreenPosition(re).vector();
		re.textureRenderer.render(
				re.imageMap.get(image),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale
		);
	}

	@Override
	public int health() {
		return health;
	}

	@Override
	public void health(int health) {
		this.health = health;
	}

	@Override
	public Inventory inventory() {
		return inventory;
	}

	@Override
	public Tile tile() {
		return tile;
	}

	@Override
	public void tile(Tile tile) {
		this.tile = tile;
		this.tileCoord = tile.coord();
	}

	// TODO perhaps reconsider having previousTile for structures
	@Override
	public Tile previousTile() {
		throw new UnsupportedOperationException("Structures do not move.");
	}

	@Override
	public void previousTile(Tile tile) {
		throw new UnsupportedOperationException("Structures do not move.");
	}

	public Effect modify(World world, Effect effect) {
		return effect;
	}

	@Override
	public void update(GameState state) {
		actionScheduler.update(state.world);
	}

	public List<ProcChain> trigger(World world, Effect effect) {
		for (Trigger trigger : triggers) {
			if (trigger.matches(world, this, effect)) {
				List<Action> actions = trigger.createActions(world, this, effect);
				for (Action action : actions) {
					queueAction(action);
				}
			}
		}
		return emptyList();
	}

	@Override
	public void queueAction(Action action) {
		actionScheduler.queue(action);
	}

	@Override
	public List<Action> actions() {
		return emptyList();
	}

	public void reinitializeAfterLoad(World world) {
		tile = world.getTile(tileCoord);
		if (inventory != null) {
			inventory.reinitializeAfterLoad(this);
		}
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Status status() {
		return status;
	}

	@Override
	public void particlePool(ParticlePool particlePool) {
		this.particlePool = particlePool;
	}

	@Override
	public ParticlePool particlePool() {
		return particlePool;
	}

}
