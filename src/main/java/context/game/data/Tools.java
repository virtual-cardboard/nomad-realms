package context.game.data;

import context.ResourcePack;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import debugui.ConsoleGui;
import debugui.SimpleMetricGui;
import util.ObservableMetric;

public final class Tools {

	public final ConsoleGui consoleGui;
	public final SimpleMetricGui<Integer> testMetricGui;
	public final ObservableMetric<Integer> metric = new ObservableMetric<>(0);

	public Tools(ResourcePack resourcePack) {
		consoleGui = new ConsoleGui(resourcePack, 0);
		testMetricGui = new SimpleMetricGui<>(resourcePack, metric, 0, 0);
		init();
	}

	private void init() {
		consoleGui.setPadding(20);
		consoleGui.setPosY(new PixelPositionConstraint(0));
		consoleGui.setWidth(new PixelDimensionConstraint(800));
		consoleGui.setHeight(new PixelDimensionConstraint(-300));
		testMetricGui.setPosX(new PixelPositionConstraint(100));
		testMetricGui.setPosY(new PixelPositionConstraint(100));
		testMetricGui.setWidth(new PixelDimensionConstraint(200));
		testMetricGui.setHeight(new PixelDimensionConstraint(200));

	}

	public void logMessage(Object object) {
		consoleGui.log(object.toString());
	}

	public void logMessage(String message) {
		consoleGui.log(message);
	}

	public void logMessage(String message, int colour) {
		consoleGui.log(message, colour);
	}

}
