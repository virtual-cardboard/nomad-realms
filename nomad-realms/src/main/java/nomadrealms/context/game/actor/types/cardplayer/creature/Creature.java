package nomadrealms.context.game.actor.types.cardplayer.creature;
import nomadrealms.context.game.interaction.InteractionState;

import engine.common.math.Vector2f;
import engine.serialization.Derializable;
import java.util.List;

import nomadrealms.context.game.card.effect.DeathEffect;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.ai.CreatureAI;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

@Derializable
public class Creature extends CardPlayer {

	private String name;
	private String image;

	/**
	 * No-arg constructor for serialization.
	 */
	protected Creature() {
	}

	public Creature(String name, Tile tile, int health, int mana, String image, DeckList deck) {
		this.name = name;
		this.tile(tile);
		this.health(health);
		this.mana(mana);
		this.maxMana(mana);
		this.image = image;
		this.deckCollection().deck1().addCards(deck.toDeck().getCards());
		this.setAi(new CreatureAI(this));
	}

	@Override
	public void update(GameState state) {
		super.update(state);
		if (!dead() && this.cardStack().getCards().isEmpty() && this.deckCollection().deck1().getCards().isEmpty()) {
			dead(true);
			state.world.addProcChain(new ProcChain(singletonList(new DeathEffect(this))));
		}
	}

	@Override
	public void render(RenderingEnvironment re, InteractionState interactionState) {
		float scale = 0.6f * TILE_RADIUS * interactionState.camera.zoom().get();
		Vector2f screenPosition = getScreenPosition(re, interactionState).vector();
		re.textureRenderer.render(
				re.imageMap.get(image),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale);
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.1f * scale,
				textFormat()
						.text(name.toUpperCase())
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (interactionState.actorTextOpacity * 255)))
						.hAlign(CENTER));
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.5f * scale,
				textFormat()
						.text(health() + " HP")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (interactionState.actorTextOpacity * 255)))
						.hAlign(CENTER));
		super.render(re, interactionState);
	}

	@Override
	public List<Appendage> appendages() {
		return emptyList();
	}

	@Override
	public boolean shouldRestock() {
		return false;
	}

	@Override
	public String name() {
		return name;
	}

}
