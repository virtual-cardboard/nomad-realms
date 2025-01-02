package nomadrealms.render.ui.content;

import nomadrealms.render.RenderingEnvironment;
import visuals.constraint.ConstraintBox;

public interface UIContent {

	UIContent parent();

	/**
	 * Get the constraint box for the content.
	 *
	 * @return the constraint box
	 */
	ConstraintBox constraintBox();

	/**
	 * Render the content.
	 * <br><br>
	 * The parent constraint box is provided to allow for relative positioning/scaling.
	 *
	 * @param re the rendering environment
	 */
	void render(RenderingEnvironment re);

}
