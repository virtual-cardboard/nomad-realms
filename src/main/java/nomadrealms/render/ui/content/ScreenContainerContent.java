package nomadrealms.render.ui.content;

import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.render.RenderingEnvironment;

/**
 * A container content that takes up the entire screen. Resizes automatically. Instantiate as many as needed, as
 * different screens may need different screen container contents.
 *
 * @author Lunkle
 */
public class ScreenContainerContent extends ContainerContent {

	/**
	 * Create a container content with the screen's constraint box.
	 * <br><br>
	 * Note that this content has no parent.
	 * <br>
	 * Whenever the screen is resized, the constraint box will be automatically updated.
	 *
	 * @param screenConstraintBox the screen's constraint box
	 */
	public ScreenContainerContent(ConstraintBox screenConstraintBox) {
		super(null, screenConstraintBox);
	}

	/**
	 * Create a container content with the screen's constraint box.
	 * <br><br>
	 * Note that this content has no parent.
	 * <br>
	 * Whenever the screen is resized, the constraint box will be automatically updated.
	 *
	 * @param re the rendering environment
	 */
	public ScreenContainerContent(RenderingEnvironment re) {
		this(re.glContext.screen);
	}

}
