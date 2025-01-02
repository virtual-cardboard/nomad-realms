package nomadrealms.render.ui.content;

import nomadrealms.render.RenderingEnvironment;
import visuals.constraint.ConstraintBox;

public class ContainerContent extends BasicUIContent {

	public ContainerContent(UIContent parent, ConstraintBox box) {
		super(parent, box);
	}

	/**
	 * Container content does not render anything.
	 *
	 * @param re the rendering environment
	 */
	@Override
	public void render(RenderingEnvironment re) {

	}

}
