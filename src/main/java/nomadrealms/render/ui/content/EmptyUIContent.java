package nomadrealms.render.ui.content;

import static visuals.constraint.posdim.AbsolutePosDimConstraint.zero;

import nomadrealms.render.RenderingEnvironment;
import visuals.constraint.ConstraintBox;

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
