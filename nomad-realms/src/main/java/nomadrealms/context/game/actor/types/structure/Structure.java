package nomadrealms.context.game.actor.types.structure;

import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import static java.util.Collections.emptyList;

import engine.common.math.Vector2f;
import java.util.List;
import java.util.UUID;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.status.Status;
import nomadrealms.context.game.actor.types.HasSpeech;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.structure.factory.StructureType;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.event.InteractEvent;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.item.Inventory;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.NullParticlePool;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.ui.custom.speech.SpeechBubble;

public abstract class Structure extends Actor {

	protected final String image;
	private final int constructionTime;
	private final int maxHealth;

	public Structure(String name, String image, int constructionTime, int health) {
		super(name, health);
		this.image = image;
		this.constructionTime = constructionTime;
		this.maxHealth = health;
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS * re.is.camera.zoom().get();
		Vector2f screenPosition = tile().getScreenPosition(re).vector();
		re.textureRenderer.render(
				re.imageMap.get(image),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale
		);
		super.render(re);
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

	public List<ProcChain> trigger(World world, Effect effect) {
		return emptyList();
	}

	public void interact(CardPlayer source) {
		System.out.println("HII " + getClass().getCanonicalName());
	}

	public int interactRange() {
		return 1;
	}

	public InteractEvent maybeInteract(GameState state, CardPlayer cardPlayer) {
		Tile playerTile = cardPlayer.tile();
		if ((tile.chunk() == playerTile.chunk() || tile.chunk().getSurroundingChunks().contains(playerTile.chunk()))
				&& tile.coord().distanceTo(playerTile.coord()) <= interactRange()) {
			return new InteractEvent(cardPlayer, this);
		}
		return null;
	}

	@Override
	public List<Action> actions() {
		return emptyList();
	}

	@Override
	public void reindex(World world) {
		super.reindex(world);
		particlePool(world.particlePool());
	}

	public abstract StructureType structureType();
}
