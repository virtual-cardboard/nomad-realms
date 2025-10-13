package nomadrealms.render.ui.content;

import java.util.List;

import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;
import engine.visuals.constraint.box.ConstraintBox;

public interface UIContent extends UI {

	UIContent parent();

	void addChild(UIContent child);

	List<UIContent> children();

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

	/**
	 * This method should be overridden to provide the actual rendering logic for this content.
	 * <br><br>
	 * Do not directly call this method; instead call {@link #render(RenderingEnvironment) render}, which also renders
	 * children content.
	 *
	 * @param re the rendering environment
	 */
	void _render(RenderingEnvironment re);

	/**
	 * Determines whether the UI element consumes user actions.
	 * <p>
	 * If this method returns true, the actions performed by the user on this UI element are considered handled, and may
	 * not propagate further. Override this method to change the default behavior.
	 *
	 * @return true if the UI element consumes actions, false otherwise
	 */
	public default boolean consumesActions() {
		return true;
	}

	void clearChildren();

}
