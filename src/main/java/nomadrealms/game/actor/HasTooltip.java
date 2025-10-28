package nomadrealms.game.actor;

import nomadrealms.game.event.Target;
import nomadrealms.render.ui.content.UIContent;
import nomadrealms.render.ui.custom.tooltip.TooltipDeterminer;

public interface HasTooltip extends Target {

	/**
	 * Double dispatch method for creating the tooltip.
	 * <br><br>
	 * When implementing, simply call the appropriate method on the determiner.
	 * <pre>
	 * {@code
	 *     @Override
	 *     public UIContent tooltip(TooltipDeterminer determiner) {
	 *         return determiner.visit(this);
	 *     }
	 * }
	 * </pre>
	 *
	 * @return the tooltip content
	 */
	public UIContent tooltip(TooltipDeterminer determiner);

}
