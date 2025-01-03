package nomadrealms.render.ui.content;

import common.math.Vector2f;

/**
 * @author Lunkle
 */
public class DynamicGridLayoutContainerContent extends ContainerContent {

	private final int gridWidth;

	public DynamicGridLayoutContainerContent(UIContent parent, Vector2f pos, int gridWidth) {
		super(parent);
//		constraintBox(new ConstraintBox(
//				pos::x, pos::y,
//				this::calculateWidth,
//				this::calculateHeight
//		)
		this.gridWidth = gridWidth;
	}

//	private float calculateWidth() {
//		List<UIContent[]> grid = new ArrayList<>();
//		for (UIContent child : children()) {
//			ConstraintBox childBox = child.constraintBox();
//			if (grid.size() % gridWidth == 0) {
//				grid.add(new UIContent[gridWidth]);
//			}
//			UIContent[] lastRow = grid.get(grid.size() - 1);
//			float rowWidth = 0;
//			for (UIContent lastRowChild : lastRow) {
//				rowWidth += lastRowChild.constraintBox().w().get();
//			}
//			if (rowWidth + childBox.w().get() > gridWidth) {
//				grid.add(new UIContent[]{child});
//			} else {
//				UIContent[] newRow = new UIContent[lastRow.length + 1];
//				System.arraycopy(lastRow, 0, newRow, 0, lastRow.length);
//				newRow[newRow.length - 1] = child;
//				grid.set(grid.size() - 1, newRow);
//			}
//		}
//	}
//
//	private float calculateHeight() {
//		UIContent[][] grid = new UIContent[0][0];
//		for (UIContent child : children()) {
//			ConstraintBox childBox = child.constraintBox();
//			if (childBox.y().get() + childBox.h().get() > constraintBox().h().get()) {
//				constraintBox().h().set(childBox.y().get() + childBox.h().get());
//			}
//		}
//	}

}
