package nomadrealms.render.ui.content;

import java.util.function.Supplier;

import nomadrealms.render.RenderingEnvironment;
import visuals.constraint.ConstraintBox;
import visuals.lwjgl.render.Texture;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class ImageContent extends BasicUIContent {

	private final Supplier<Texture> image;

	public ImageContent(Supplier<Texture> image, UIContent parent, ConstraintBox box) {
		super(parent, box);
		this.image = image;
	}

	@Override
	public void render(RenderingEnvironment re) {
		DefaultFrameBuffer.instance().render(() -> {
			re.textureRenderer.render(
					image.get(),
					constraintBox().x().get(), constraintBox().y().get(),
					constraintBox().w().get(), constraintBox().h().get());
		});
	}

}
