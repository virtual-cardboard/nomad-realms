package nomadrealms.particles;

import nomadrealms.render.RenderingEnvironment;

public interface Particle {

	void update(float deltaTime);

	void render(RenderingEnvironment re);

	boolean isAlive();

	void onDeath();

}
