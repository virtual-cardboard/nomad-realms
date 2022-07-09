package context.synctime;

import static context.visuals.colour.Colour.rgb;

import context.visuals.GameVisuals;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.gui.renderer.RootGuiRenderer;
import debugui.RollingAverageStat;

public class SyncTimeVisuals extends GameVisuals {

	private RootGuiRenderer rootGuiRenderer;
	private SyncTimeData data;

	@Override
	public void init() {
		data = (SyncTimeData) context().data();
		rootGui().addChild(data.rttStat());
//		new LabelGui(resourcePack().getRenderer("rectangleRenderer", RectangleRenderer.class), res)
		RollingAverageStat timeOffsetStat = data.timeOffsetStat();
		timeOffsetStat.setPosX(new PixelPositionConstraint(450));
		rootGui().addChild(timeOffsetStat);
		rootGui().addChild(data.tools().consoleGui);

		rootGuiRenderer = resourcePack().getRenderer("rootGui", RootGuiRenderer.class);
	}

	@Override
	public void render() {
		background(rgb(50, 168, 162));
		rootGuiRenderer.render(data, rootGui());
	}

}
