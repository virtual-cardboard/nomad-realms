package nomadrealms.render.ui.content;

import static common.colour.Colour.rgb;

import java.util.function.Supplier;

import nomadrealms.render.RenderingEnvironment;
import visuals.constraint.ConstraintBox;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class TextContent extends BasicUIContent {

	private final Supplier<String> text;

	public TextContent(String text, UIContent parent, ConstraintBox box) {
		this(() -> text, parent, box);
	}

	public TextContent(Supplier<String> text, UIContent parent, ConstraintBox box) {
		super(parent, box);
		this.text = text;
	}

	@Override
	public void _render(RenderingEnvironment re) {
		DefaultFrameBuffer.instance().render(() -> {
			re.textRenderer.render(
					constraintBox().x().get(), constraintBox().y().get(),
					text.get(),
					constraintBox().w().get(),
					re.font, 20,
					rgb(255, 255, 255));
		});
	}

}
