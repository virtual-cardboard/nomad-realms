package nomadrealms.render.ui.content;

import java.util.function.Supplier;

import nomadrealms.render.RenderingEnvironment;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class ImageContent extends BasicUIContent {

	private final Supplier<Texture> image;

	public ImageContent(Supplier<Texture> image, UIContent parent, ConstraintBox box) {
		super(parent, box);
		this.image = image;
	}

	@Override
	public void _render(RenderingEnvironment re) {
		DefaultFrameBuffer.instance().render(() -> {
			re.textureRenderer.render(
					image.get(),
					constraintBox().x().get(), constraintBox().y().get(),
					constraintBox().w().get(), constraintBox().h().get());
		});
	}

}
