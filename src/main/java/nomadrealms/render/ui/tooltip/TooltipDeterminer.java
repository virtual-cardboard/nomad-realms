package nomadrealms.render.ui.tooltip;

import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.HUMIDITY_CEIL;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.HUMIDITY_FLOOR;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.TEMPERATURE_CEIL;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.TEMPERATURE_FLOOR;

import nomadrealms.game.actor.HasTooltip;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.generation.status.biome.BiomeParameters;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.Tooltip;
import nomadrealms.render.ui.content.ContainerContent;
import nomadrealms.render.ui.content.TextContent;
import nomadrealms.render.ui.content.TileSpotlightContent;
import nomadrealms.render.ui.content.UIContent;

/**
 * The double-dispatch class for determining tooltip UI.
 */
public class TooltipDeterminer {

	private Tooltip tooltip;
	private RenderingEnvironment re;

	/**
	 * Create a new tooltip determiner.
	 *
	 * @param re the rendering environment
	 */
	public TooltipDeterminer(Tooltip tooltip, RenderingEnvironment re) {
		this.tooltip = tooltip;
		this.re = re;
	}

	public UIContent visit(HasTooltip object) {
		ContainerContent container = tooltip.uiContainer();
		return new TextContent("Overload this tooltip in the TooltipDeterminer class.", container,
				container.constraintBox());
	}

	public UIContent visit(Tile tile) {
		ContainerContent container = tooltip.uiContainer();
		container.addChild(new TileSpotlightContent(tile, container, container.constraintBox()));
		container.addChild(new TextContent("Tile", container, container.constraintBox()));
		StringBuffer sb = new StringBuffer();
		sb.append("Tile coordinates: ").append(tile.coord());
		sb.append("\n");
		Zone zone = tile.zone();
		BiomeParameters p = zone.biomeGenerationStep().parametersAt(tile.coord());
		sb.append("Tile elevation: ").append(p);

		System.out.println();
		System.out.println("================================");
		System.out.println(tile.coord());
		System.out.println(p);
		float adjustedTemperature =
				(p.temperature() + 1) * (TEMPERATURE_CEIL - TEMPERATURE_FLOOR) / 2 + TEMPERATURE_FLOOR;
		float adjustedHumidity = (p.humidity() + 1) * (HUMIDITY_CEIL - HUMIDITY_FLOOR) / 2 + HUMIDITY_FLOOR;
		System.out.println("adjusted temperature: " + adjustedTemperature);
		System.out.println("adjusted humidity: " + adjustedHumidity);
		System.out.println(zone.biomeGenerationStep().continentAt(tile.coord()));
		System.out.println(zone.biomeGenerationStep().categoryAt(tile.coord()));
		System.out.println(zone.biomeGenerationStep().biomeAt(tile.coord()));
		return container;
	}

}
