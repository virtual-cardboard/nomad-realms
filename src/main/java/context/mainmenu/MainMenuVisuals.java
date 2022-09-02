package context.mainmenu;

import context.ResourcePack;
import context.game.visuals.renderer.ParticleRenderer;
import context.visuals.GameVisuals;
import context.visuals.colour.Colour;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.gui.renderer.RootGuiRenderer;
import context.visuals.lwjgl.Texture;
import engine.common.math.Vector2f;
import graphics.particle.TextureParticle;
import graphics.particle.function.DeceleratingRotationFunction;
import graphics.particle.function.DeceleratingTransformation;
import graphics.particle.function.ParabolicTransformation;
import graphics.particle.function.StaticColourFunction;

public class MainMenuVisuals extends GameVisuals {

	private RootGuiRenderer rootGuiRenderer;
	private ParticleRenderer particleRenderer;

	private Texture hexagonTex;

	private int time;

	@Override
	public void init() {
		ResourcePack rp = resourcePack();
		particleRenderer = rp.getRenderer("particle", ParticleRenderer.class);
		rootGuiRenderer = rp.getRenderer("rootGui", RootGuiRenderer.class);
		hexagonTex = rp.getTexture("particle_hexagon");

		StartButton startButton = new StartButton(rp, ((MainMenuLogic) context().logic()), ((MainMenuData) context().data()));
		startButton.setPosX(new CenterPositionConstraint(startButton.width()));
		startButton.setPosY(new CenterPositionConstraint(startButton.height()));
		rootGui().addChild(startButton);
		rootGui().addChild(((MainMenuData) context().data()).tools().consoleGui);

		MainMenuData data = (MainMenuData) context().data();
		data.tools().logMessage("This is the console gui! Press 'T' to toggle it.", 0xfcba03ff);
	}

	@Override
	public void render() {
		background(Colour.rgb(66, 245, 99));
		rootGuiRenderer.render(context().data(), rootGui());
		particleRenderer.renderParticles();
		if (time != 3) {
			time++;
			return;
		}
		int numParticles = (int) (Math.random() * 2);
		for (int i = 0; i < numParticles; i++) {
			generateParticle();
		}
	}

	private void generateParticle() {
		TextureParticle p = new TextureParticle();
		p.lifetime = 800;
		p.tex = hexagonTex;
		float x = Math.abs(rand(rootGui().widthPx()) + 200) - 200;
		float dist = Math.abs(rand(4)) + 5;
		p.xFunc = new DeceleratingTransformation(x, rand(0.9f) + 2f, 0.0001f);
		p.yFunc = new ParabolicTransformation(-30, dist * 0.80f, 0.004f * dist);
		p.rotFunc = new DeceleratingRotationFunction(rand(2) + 2, rand(0.3f), 0.02f);

		int[] possibleColours = { 0x4e5566FF, 0xde6210FF, 0x6b6b6bFF, 0x92a2a8FF, 0x73241cFF };
		int colour = possibleColours[(int) (Math.random() * possibleColours.length)];
		p.colourFunc = new StaticColourFunction(colour);
		p.dim = new Vector2f(dist * 2, dist * 2);
		particleRenderer.particles().add(p);
		time = 0;
	}

	private float rand(float scale) {
		return (float) ((Math.random() - 0.5) * 2) * scale;
	}

}
