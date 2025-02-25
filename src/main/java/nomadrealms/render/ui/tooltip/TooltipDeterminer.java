package nomadrealms.render.ui.tooltip;

import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.HUMIDITY_CEIL;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.HUMIDITY_FLOOR;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.TEMPERATURE_CEIL;
import static nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeCategory.TEMPERATURE_FLOOR;
import static visuals.constraint.posdim.AbsoluteConstraint.zero;

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
		return new TextContent("Overload this tooltip in the TooltipDeterminer class.",
				100, 20, re.font,
				container.constraintBox().coordinate());
	}

	public UIContent visit(Tile tile) {
		ContainerContent container = tooltip.uiContainer();

		StringBuffer sb = new StringBuffer();
		TileSpotlightContent tileSpotlight = new TileSpotlightContent(tile, container.constraintBox().coordinate());
		container.addChild(tileSpotlight);
		container.addChild(new TextContent("Tile",
				50, 20, re.font,
				container.constraintBox().coordinate().translate(tileSpotlight.constraintBox().w(), zero())));
		sb.append("Tile coordinates: ").append(tile.coord()).append("\n");
		Zone zone = tile.zone();
		BiomeParameters p = zone.biomeGenerationStep().parametersAt(tile.coord());

		float adjustedTemperature = (p.temperature() + 1) * (TEMPERATURE_CEIL - TEMPERATURE_FLOOR) / 2
				+ TEMPERATURE_FLOOR;
		float adjustedHumidity = (p.humidity() + 1) * (HUMIDITY_CEIL - HUMIDITY_FLOOR) / 2 + HUMIDITY_FLOOR;
		sb.append("Biome stats:").append("\n");
		sb.append("\tBiome: ").append(zone.biomeGenerationStep().biomeAt(tile.coord())).append("\n");
		sb.append("\tBiome category: ").append(zone.biomeGenerationStep().categoryAt(tile.coord())).append("\n");
		sb.append("\tContinent: ").append(zone.biomeGenerationStep().continentAt(tile.coord())).append("\n");
		sb.append("\tContinentalness: ").append(p.continentalness()).append("\n");
		sb.append("\tHumidity: ").append(adjustedHumidity).append("cm of Preciptation\n");
		sb.append("\tTemperature: ").append(adjustedTemperature).append(" C\n"); // ("\u00B0 C\n");
		sb.append("\tDepth: ").append(p.depth()).append("\n");

		TextContent stats = new TextContent(sb.toString(),
				500, 15, re.font,
				container.constraintBox().coordinate().translate(zero(), tileSpotlight.constraintBox().h()), 10);
		container.addChild(stats);
		return container;
	}

}
