package nomadrealms.render.ui.content;

import java.util.Collection;

import nomadrealms.render.RenderingEnvironment;
import visuals.constraint.ConstraintBox;

public interface UIContent {

	UIContent parent();

	void addChild(UIContent child);

	Collection<UIContent> children();

	/**
	 * Get the constraint box for the content.
	 *
	 * @return the constraint box
	 */
	ConstraintBox constraintBox();

	/**
	 * Render the content. Also renders all children.
	 * <br><br>
	 * The parent constraint box is provided to allow for relative positioning/scaling.
	 *
	 * @param re the rendering environment
	 */
	default void render(RenderingEnvironment re) {
		_render(re);
		for (UIContent child : children()) {
			child.render(re);
		}
	}

	void _render(RenderingEnvironment re);

	void clearChildren();

}
