package nomadrealms.render.ui.content;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import java.util.ArrayList;
import java.util.List;

import engine.visuals.constraint.box.ConstraintBox;


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
	private final List<UIContent> children = new ArrayList<>();

	/**
	 * Common mistake is to call this constructor while also calling {@link UIContent#addChild(UIContent)}, which will
	 * result in the child being added to the parent twice.
	 *
	 * @param parent the parent content
	 */
	public BasicUIContent(UIContent parent) {
		this(parent, new ConstraintBox(absolute(0), absolute(0), absolute(0), absolute(0)));
	}

	public BasicUIContent(ConstraintBox constraintBox) {
		this(null, constraintBox);
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
	public List<UIContent> children() {
		return children;
	}

	@Override
	public ConstraintBox constraintBox() {
		return constraintBox;
	}

	public BasicUIContent constraintBox(ConstraintBox box) {
		this.constraintBox = box;
		return this;
	}

	@Override
	public void clearChildren() {
		children.clear();
	}

}
