package nomadrealms.render.ui.content;

import static engine.common.colour.Colour.rgb;

import java.util.function.Supplier;

import engine.common.math.Vector2f;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.rendering.text.GameFont;
import engine.visuals.rendering.text.TextRenderer;
import nomadrealms.render.RenderingEnvironment;

public class TextContent extends BasicUIContent {

	private final Supplier<String> text;
	private final float lineWidth;
	private final int fontSize;
	private final GameFont font;
	private final float padding;

	public TextContent(String text, float lineWidth, int fontSize, GameFont font, ConstraintPair coord) {
		this(() -> text, lineWidth, fontSize, font, coord, 0);
	}

	public TextContent(String text, float lineWidth, int fontSize, GameFont font, ConstraintPair coord, float padding) {
		this(() -> text, lineWidth, fontSize, font, coord, padding);
	}

	public TextContent(Supplier<String> text, float lineWidth, int fontSize, GameFont font,
					   ConstraintPair coord, float padding) {
		super(new ConstraintBox(coord,
				new ConstraintPair(
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
		re.textRenderer.alignLeft().alignTop();
		re.textRenderer.render(
				constraintBox().x().get() + padding, constraintBox().y().get() + padding,
				text.get(),
				lineWidth,
				font, fontSize,
				rgb(255, 255, 255));
	}

}
