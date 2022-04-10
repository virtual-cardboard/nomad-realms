package context.mainmenu;

import static context.visuals.colour.Colour.rgb;
import static context.visuals.colour.Colour.rgba;
import static context.visuals.renderer.TextRenderer.ALIGN_CENTER;

import java.util.ArrayList;
import java.util.List;

import context.ResourcePack;
import context.game.visuals.renderer.ParticleRenderer;
import context.visuals.GameVisuals;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.colour.Colour;
import context.visuals.gui.Gui;
import context.visuals.gui.LabelGui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.gui.renderer.RootGuiRenderer;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import engine.common.math.Vector2f;
import graphics.particle.Particle;
import graphics.particle.TextureParticle;
import graphics.particle.function.DeceleratingRotationFunction;
import graphics.particle.function.DeceleratingTransformation;
import graphics.particle.function.ParabolicTransformation;
import graphics.particle.function.StaticColourFunction;

public class MainMenuVisuals extends GameVisuals {

	private RootGuiRenderer rootGuiRenderer;
	private ParticleRenderer particleRenderer;
	private LabelGui startButton;

	private List<Particle> particles = new ArrayList<>();
	private Texture hexagonTex;

	private int time;

	@Override
	public void init() {
		ResourcePack rp = resourcePack();
		particleRenderer = rp.getRenderer("particle", ParticleRenderer.class);
		hexagonTex = rp.getTexture("particle_hexagon");
		startButton = new LabelGui(rp.getRenderer("rectangle", RectangleRenderer.class), rp.getRenderer("text", TextRenderer.class), rp.getFont("langar"),
				"Start", 30, 255, rgb(245, 220, 152));
		startButton.align = ALIGN_CENTER;
		startButton.paddingTop = 20;
		startButton.setWidth(new PixelDimensionConstraint(100));
		startButton.setHeight(new PixelDimensionConstraint(60));
		startButton.setPosX(new CenterPositionConstraint(startButton.width()));
		startButton.setPosY(new CenterPositionConstraint(startButton.height()));
		rootGui().addChild(startButton);
		rootGuiRenderer = new RootGuiRenderer();
	}

	@Override
	public void render() {
		background(Colour.rgb(66, 245, 99));
		rootGuiRenderer.render(glContext(), context().data(), rootGui());
		for (int i = particles.size() - 1; i >= 0; i--) {
			Particle p = particles.get(i);
			if (p.isDead()) {
				particles.remove(i);
				continue;
			}
			particleRenderer.renderParticle(p);
		}
		if (time != 3) {
			time++;
			return;
		}
		TextureParticle p = new TextureParticle();
		p.lifetime = 800;
		p.tex = hexagonTex;
		float x = Math.abs(rand(2000)) + rand(100);
		float dist = Math.abs(rand(10)) + 1;
		p.xFunc = new DeceleratingTransformation(x, rand(0.9f), 0.01f);
		p.yFunc = new ParabolicTransformation(-30, dist * 0.12f, 0.004f * dist);
		p.rotFunc = new DeceleratingRotationFunction(rand(4), rand(0.3f), 0.1f);
		p.colourFunc = new StaticColourFunction(rgba(222, 250, 247, 180));
		p.dim = new Vector2f(dist * 2, dist * 2);
		particles.add(p);
		time = 0;
	}

	private float rand(float scale) {
		return (float) ((Math.random() - 0.5) * 2) * scale;
	}

	public Gui startButton() {
		return startButton;
	}

}
