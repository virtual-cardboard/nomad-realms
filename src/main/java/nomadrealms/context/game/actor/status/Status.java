package nomadrealms.context.game.actor.status;

import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;

import java.util.ArrayList;
import java.util.List;

import engine.common.colour.Colour;
import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.render.RenderingEnvironment;

public class Status {

	private final int[] stacks = new int[StatusEffect.values().length];

	public void add(StatusEffect effect, int count) {
		stacks[effect.ordinal()] += count;
	}

	public void remove(StatusEffect effect, int count) {
		stacks[effect.ordinal()] -= count;
	}

	public int count(StatusEffect effect) {
		return stacks[effect.ordinal()];
	}

	public void render(RenderingEnvironment re, ConstraintPair position) {
		float x = position.x().get();
		float y = position.y().get();
		List<StatusEffect> activeEffects = new ArrayList<>();
		for (StatusEffect effect : StatusEffect.values()) {
			if (count(effect) > 0) {
				activeEffects.add(effect);
			}
		}
		for (int i = 0; i < activeEffects.size(); i++) {
			StatusEffect status = activeEffects.get(i);
			float iconSize = 10f;
			float iconX = x - (activeEffects.size() * iconSize) / 2 + i * iconSize;
			float iconY = y - TILE_VERTICAL_SPACING / 2;
			re.textureRenderer.render(re.imageMap.get(status.image()), iconX, iconY, iconSize, iconSize);
			re.textRenderer.alignRight().alignBottom();
			re.textRenderer.render(
					iconX + iconSize,
					iconY + iconSize,
					String.valueOf(count(status)),
					0, // No line wrap
					re.font,
					10f, // Font size in pixels
					Colour.rgb(255, 255, 255)
			);
		}
	}

}
