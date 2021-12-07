package model.particle;

import common.math.Vector2f;
import context.visuals.lwjgl.Texture;

public class Particle {

	public Vector2f pos;
	public Vector2f dim;

	public Vector2f vel;
	public Vector2f acc;

	public float rot;// Rotation, not to be confused with decay
	public float rotVel;

	public Texture tex;

	public int lifetime;
	public int fadeStart;

	public Particle(Vector2f pos, Vector2f dim, float rot, Texture tex, int lifetime, int fadeStart) {
		this.pos = pos;
		this.dim = dim;
		this.rot = rot;
		this.tex = tex;
		this.lifetime = lifetime;
		this.fadeStart = fadeStart;
	}

}
