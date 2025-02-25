package nomadrealms.render.ui.content;

import static common.colour.Colour.rgb;

import java.util.function.Supplier;

import common.math.Vector2f;
import nomadrealms.render.RenderingEnvironment;
import visuals.constraint.box.ConstraintBox;
import visuals.constraint.box.ConstraintCoordinate;
import visuals.constraint.box.ConstraintSize;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.rendering.text.GameFont;
import visuals.rendering.text.TextRenderer;

public class TextContent extends BasicUIContent {

	private float padding;

	private final Supplier<String> text;
	private final float lineWidth;
	private final int fontSize;
	private final GameFont font;

	public TextContent(String text, float lineWidth, int fontSize, GameFont font, ConstraintCoordinate coord) {
		this(() -> text, lineWidth, fontSize, font, coord, 0);
	}

	public TextContent(String text, float lineWidth, int fontSize, GameFont font, ConstraintCoordinate coord, float padding) {
		this(() -> text, lineWidth, fontSize, font, coord, padding);
	}

	public TextContent(Supplier<String> text, float lineWidth, int fontSize, GameFont font,
					   ConstraintCoordinate coord, float padding) {
		super(new ConstraintBox(coord,
				new ConstraintSize(
						TextRenderer.calculateTextSize(
										text.get(),
										lineWidth,
										font,
										fontSize)
								.add(new Vector2f(padding, padding).scale(2)))
		));
		this.text = text;
		this.lineWidth = lineWidth;
		this.fontSize = fontSize;
		this.font = font;
		this.padding = padding;
	}

	@Override
	public void _render(RenderingEnvironment re) {
		DefaultFrameBuffer.instance().render(() -> {
			re.textRenderer.render(
					constraintBox().x().get() + padding, constraintBox().y().get() + padding,
					text.get(),
					lineWidth,
					font, fontSize,
					rgb(255, 255, 255));
		});
	}

}
