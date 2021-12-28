
package context.game.visuals.displayable;

import common.math.Vector2f;
import context.GLContext;
import context.game.visuals.renderer.ActorBodyPartRenderer;
import context.visuals.lwjgl.Texture;

public class TextureBodyPart extends ActorBodyPart {

	public Texture tex;
	public float texScale = 1;

	public TextureBodyPart(Texture tex, float texScale) {
		this.tex = tex;
		this.texScale = texScale;
	}

	@Override
	public void render(ActorBodyPartRenderer bodyPartRenderer, GLContext glContext, Vector2f screenDim, Vector2f position, Vector2f direction) {
		bodyPartRenderer.renderTextureBodyPart(glContext, screenDim, this, position, direction);
	}

}
