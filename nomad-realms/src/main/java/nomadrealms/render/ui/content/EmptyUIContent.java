package nomadrealms.render.ui.content;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.zero;

import nomadrealms.render.RenderingEnvironment;
import engine.visuals.constraint.box.ConstraintBox;

public class EmptyUIContent extends BasicUIContent {

	public EmptyUIContent() {
		this(new ConstraintBox(zero(), zero(), zero(), zero()));
	}

	public EmptyUIContent(ConstraintBox constraintBox) {
		super(constraintBox);
	}

	@Override
	public void _render(RenderingEnvironment re) {
	}

}
