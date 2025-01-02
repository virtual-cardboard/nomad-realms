package nomadrealms.render.ui.content;

import nomadrealms.render.RenderingEnvironment;

/**
 * A container content that takes up the entire screen.
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
	 * @param re the rendering environment to get the screen constraint box from
	 */
	public ScreenContainerContent(RenderingEnvironment re) {
		super(null, re.glContext.screen);
	}

}
