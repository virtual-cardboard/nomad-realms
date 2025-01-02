package nomadrealms.render.ui.content;

import visuals.constraint.ConstraintBox;

/**
 * Basic UI content for fixed-size UI elements.
 */
public abstract class BasicUIContent implements UIContent {

	private final ConstraintBox constraintBox;
	private final UIContent parent;

	/**
	 * Create a new basic UI content.
	 *
	 * @param constraintBox the constraint box for the content
	 */
	public BasicUIContent(UIContent parent, ConstraintBox constraintBox) {
		this.parent = parent;
		this.constraintBox = constraintBox;
	}

	@Override
	public UIContent parent() {
		return parent;
	}

	@Override
	public ConstraintBox constraintBox() {
		return constraintBox;
	}

}
