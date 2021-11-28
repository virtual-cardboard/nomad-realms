package context.bootstrap;

import static context.visuals.colour.Colour.rgb;

import context.visuals.GameVisuals;

public class BootstrapGameVisuals extends GameVisuals {

	@Override
	public void render() {
		background(rgb(64, 134, 23));
	}

}
