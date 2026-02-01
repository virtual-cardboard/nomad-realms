package nomadrealms.render.ui.custom.card;

import engine.common.math.Vector2f;
import engine.context.input.Mouse;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.target.TargetType;
import nomadrealms.context.game.card.target.TargetingInfo;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class TargetingArrow implements UI {

	private UICard origin;
	private Mouse mouse;
	private TargetingInfo info;
	private Target target;

	GameState state;

	public TargetingArrow(GameState state) {
		this.state = state;
	}

	@Override
	public void render(RenderingEnvironment re) {
		target = null;
		if (origin == null || mouse == null) {
			return;
		}

		Tile tile = state.getMouseHexagon(mouse, re.camera);
		Vector2f screenPosition = null;

		if (info.targetType() == TargetType.HEXAGON) {
			if (!checkConditions(info, state.world(), tile, state.world().nomad)) {
				return;
			}
			target = tile;
			screenPosition = tile.getScreenPosition(re).vector();
		} else if (info.targetType() == TargetType.CARD_PLAYER) {
			if (tile.actor() == null || !checkConditions(info, state.world(), tile.actor(), state.world().nomad)) {
				return;
			}
			target = tile.actor();
			screenPosition = tile.getScreenPosition(re).vector();
		}

		new Arrow(origin.centerPosition().vector(), mouse.coordinate().vector()).targetCenter(screenPosition).render(re);
	}

	private boolean checkConditions(TargetingInfo info, World world, Target target, CardPlayer source) {
		return info.conditions().stream().allMatch(c -> c.test(world, target, source));
	}

	public TargetingArrow origin(UICard origin) {
		this.origin = origin;
		return this;
	}

	public TargetingArrow mouse(Mouse mouse) {
		this.mouse = mouse;
		return this;
	}

	/**
	 * May be race condition, keep an eye on this.
	 *
	 * @param targetingInfo
	 * @return
	 */
	public TargetingArrow info(TargetingInfo targetingInfo) {
		this.info = targetingInfo;
		return this;
	}

	public Target target() {
		return target;
	}

	public void target(Target target) {
		this.target = target;
	}

}
