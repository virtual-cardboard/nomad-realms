package nomadrealms.particles;

import nomadrealms.render.RenderingEnvironment;

public interface Particle {

	void render(RenderingEnvironment re, long timestamp);

	long lifetime();

	void onDeath();

}
