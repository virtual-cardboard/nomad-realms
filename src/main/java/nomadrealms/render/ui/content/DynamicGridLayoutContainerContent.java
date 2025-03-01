package nomadrealms.render.ui.content;

import static java.lang.Math.ceil;
import static java.lang.Math.max;

import visuals.constraint.box.ConstraintBox;
import visuals.constraint.box.ConstraintCoordinate;


/**
 * @author Lunkle
 */
public class DynamicGridLayoutContainerContent extends ContainerContent {

	private final int gridWidth;

	public DynamicGridLayoutContainerContent(UIContent parent, ConstraintCoordinate coord, int gridWidth) {
		super(parent);
		constraintBox(new ConstraintBox(
				coord,
				this::calculateWidth,
				this::calculateHeight
		));
		this.gridWidth = gridWidth;
	}

	private float calculateWidth() {
		float maxWidth = 0;
		for (int i = 0; i < (int) ceil((double) children().size() / gridWidth); i++) {
			float rowWidth = 0;
			for (int j = 0; j < gridWidth; j++) {
				if (i * gridWidth + j >= children().size()) {
					break;
				}
				rowWidth += children().get(i * gridWidth + j).constraintBox().w().get();
			}
			maxWidth = max(maxWidth, rowWidth);
		}
		return maxWidth;
	}

	private float calculateHeight() {
		float height = 0;
		for (int i = 0; i < (int) ceil((double) children().size() / gridWidth); i++) {
			float rowHeight = 0;
			for (int j = 0; j < gridWidth; j++) {
				if (i * gridWidth + j >= children().size()) {
					break;
				}
				float h = children().get(i * gridWidth + j).constraintBox().h().get();
				if (h > rowHeight) {
					rowHeight = h;
				}
			}
			height += rowHeight;
		}
		return height;
	}

	//	@Override
	//	public void render(RenderingEnvironment re) {
	//		super.render(re);
	//	}

}
