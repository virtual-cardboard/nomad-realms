package context.game.visuals.displayable;

import context.visuals.lwjgl.Texture;

public class ActorBodyPart {

	public Texture tex;
	public float height;
	public float dist;
	public float minScale;
	public float rot;
	public float texScale = 1;

	public ActorBodyPart(Texture tex, float height, float dist, float minScale) {
		this.tex = tex;
		this.height = height;
		this.dist = dist;
		this.minScale = minScale;
	}

}
