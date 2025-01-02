package nomadrealms.render.ui.tooltip;

import nomadrealms.game.actor.HasTooltip;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.Tooltip;
import nomadrealms.render.ui.content.ContainerContent;
import nomadrealms.render.ui.content.TextContent;
import nomadrealms.render.ui.content.UIContent;

/**
 * The double-dispatch class for determining tooltip UI.
 */
public class TooltipDeterminer {

	private Tooltip tooltip;
	private RenderingEnvironment re;

	/**
	 * Create a new tooltip determiner.
	 *
	 * @param re the rendering environment
	 */
	public TooltipDeterminer(Tooltip tooltip, RenderingEnvironment re) {
		this.tooltip = tooltip;
		this.re = re;
	}

	public UIContent visit(HasTooltip object) {
		ContainerContent container = tooltip.uiContainer();
		return new TextContent("hi", container, container.constraintBox());
	}

}
