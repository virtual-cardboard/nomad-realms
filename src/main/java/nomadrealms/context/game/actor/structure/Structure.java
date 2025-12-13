package nomadrealms.context.game.actor.structure;

import static java.util.Collections.emptyList;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import java.util.List;

import engine.common.math.Vector2f;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.item.Inventory;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.posdim.AbsoluteConstraint;
import nomadrealms.render.ui.constraint.ActorScreenPositionConstraint;

public class Structure implements Actor {

	private transient ConstraintBox box;

	private TileCoordinate tileCoord;
	private transient Tile tile;
	private Inventory inventory;

	private final String name;
	private final String image;
	private final int constructionTime;
	private final int maxHealth;
	private int health;

	public Structure(String name, String image, int constructionTime, int health) {
		this.name = name;
		this.image = image;
		this.constructionTime = constructionTime;
		this.maxHealth = health;
		this.health = health;
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (box == null) {
			float scale = 0.6f * TILE_RADIUS;
			Constraint w = new AbsoluteConstraint(scale);
			Constraint h = new AbsoluteConstraint(scale);
			Constraint x = new ActorScreenPositionConstraint(this, re, true).add(w.multiply(0.5f).neg());
			Constraint y = new ActorScreenPositionConstraint(this, re, false).add(h.multiply(0.7f).neg());
			box = new ConstraintBox(x, y, w, h);
		}
		DefaultFrameBuffer.instance().render(
				() -> {
					re.textureRenderer.render(
							re.imageMap.get(image),
							box
					);
				}
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

	public Intent modify(World world, Intent intent) {
		return intent;
	}

	public List<ProcChain> trigger(World world, Intent intent) {
		return emptyList();
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

}
