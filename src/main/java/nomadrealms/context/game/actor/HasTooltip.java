package nomadrealms.context.game.actor;

import static java.util.Collections.emptyList;

import java.util.List;

import engine.common.java.Pair;
import nomadrealms.render.ui.content.UIContent;
import nomadrealms.render.ui.custom.tooltip.TooltipDeterminer;

public interface HasTooltip {

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

	default List<Pair<String, String>> getTooltipEntries() {
		return emptyList();
	}
}
