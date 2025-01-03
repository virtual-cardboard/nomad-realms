package nomadrealms.render.ui.content;

import static visuals.constraint.posdim.AbsolutePosDimConstraint.absolute;

import java.util.ArrayList;
import java.util.Collection;

import visuals.constraint.ConstraintBox;

/**
 * Basic UI content for fixed-size UI elements.
 * <br><br>
 * This class provides a basic implementation of the UI content interface. It provides a constraint box for the content
 * and a parent content. It also provides a collection of children content.
 * <br><br>
 * It does not provide any rendering functionality.
 *
 * @author Lunkle
 * @see UIContent
 * @see ConstraintBox
 */
public abstract class BasicUIContent implements UIContent {

	private ConstraintBox constraintBox;
	private final UIContent parent;
	private final Collection<UIContent> children = new ArrayList<>();

	public BasicUIContent(UIContent parent) {
		this(parent, new ConstraintBox(absolute(0), absolute(0), absolute(0), absolute(0)));
	}

	/**
	 * Create a new basic UI content.
	 *
	 * @param constraintBox the constraint box for the content
	 */
	public BasicUIContent(UIContent parent, ConstraintBox constraintBox) {
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
		}
		this.constraintBox = constraintBox;
	}

	@Override
	public UIContent parent() {
		return parent;
	}

	@Override
	public void addChild(UIContent child) {
		children.add(child);
	}

	@Override
	public Collection<UIContent> children() {
		return children;
	}

	@Override
	public ConstraintBox constraintBox() {
		return constraintBox;
	}

	public void constraintBox(ConstraintBox box) {
		this.constraintBox = box;
	}

}
