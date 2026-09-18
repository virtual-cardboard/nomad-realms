package nomadrealms.context.game.actor;

import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.actor.status.StatusEffect.INVINCIBLE;

import engine.visuals.constraint.box.ConstraintPair;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.status.Status;
import nomadrealms.context.game.actor.types.HasHealth;
import nomadrealms.context.game.actor.types.HasInventory;
import nomadrealms.context.game.actor.types.HasPosition;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.card.effect.SpawnParticlesEffect;
import nomadrealms.context.game.card.query.actor.SelfQuery;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.item.Inventory;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.actor.types.HasSpeech;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.Renderable;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.NullParticlePool;
import nomadrealms.render.ui.custom.speech.SpeechBubble;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.particle.spawner.BasicParticleSpawner;

/**
 * An entity in the game world. Actors can have health, a position, an inventory, and can be targeted by actions. They
 * can also be rendered on the screen.
 *
 * @author Lunkle
 */
public abstract class Actor implements HasPosition, HasHealth, HasInventory, Target, Renderable, HasSpeech {

	private final UUID uuid = UUID.randomUUID();
	private transient ParticlePool particlePool = new NullParticlePool();
	private transient SpeechBubble speech = new SpeechBubble(this);

	protected String name;
	protected int health;
	protected boolean dead;

	protected TileCoordinate tileCoord;
	protected transient Tile tile;

	protected final Status status = new Status();
	protected Inventory inventory = new Inventory(this);

	public Actor() {
	}

	public Actor(String name, int health) {
		this.name = name;
		this.health = health;
	}

	public UUID uuid() {
		return uuid;
	}

	public String name() {
		return name;
	}

	public List<Action> actions() {
		return new ArrayList<>();
	}

	public void update(GameState state) {
	}

	public List<InputEvent> retrieveNextPlays() {
		return new ArrayList<>();
	}

	public boolean dead() {
		return dead;
	}

	public void dead(boolean dead) {
		this.dead = dead;
	}

	public Status status() {
		return status;
	}

	@Override
	public void render(RenderingEnvironment re) {
		speech().render(re);
	}

	@Override
	public SpeechBubble speech() {
		return speech;
	}

	@Override
	public void damage(int damage) {
		if (damage > 0 && status().count(INVINCIBLE) > 0) {
			status().remove(INVINCIBLE, 1);
			particlePool().addParticles(new SpawnParticlesEffect(
					this,
					new BasicParticleSpawner(new SelfQuery<>(), "text_blocked")
							.size((i, source, target) -> new ConstraintPair(absolute(10), absolute(10)))
							.position((i, source, target) -> target.tile().pos().add(absolute(0), time().neg().multiply(0.1f)))
							.lifetime((i, source, target) -> 500L),
					new EffectContext().source(this).target(this)
			));
			return;
		}
		HasHealth.super.damage(damage);
	}

	public void particlePool(ParticlePool particlePool) {
		this.particlePool = particlePool;
	}

	public ParticlePool particlePool() {
		return particlePool;
	}

	/**
	 * @return Whether this actor has the capability of restocking cards when they run out, or if they will simply die
	 * once they are out of cards.
	 */
	public boolean shouldRestock() {
		return true;
	}

	public ConstraintPair getScreenPosition(RenderingEnvironment re) {
		return tile().getScreenPosition(re);
	}

	/**
	 * purely done for the sake of adding references to optimize other algorithms
	 */
	public void reindex(World world) {
		tile = world.getTile(tileCoord);
		if (inventory != null) {
			inventory.reindex(this);
		}
	}

	@Override
	public boolean move(Tile target) {
		if (target.actor() != null) {
			return false;
		}
		if (tile() != null) {
			tile().clearActor();
		}
		if (!HasPosition.super.move(target)) {
			return false;
		}
		target.actor(this);
		return true;
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
		this.tileCoord = (tile == null) ? null : tile.coord();
	}

}
