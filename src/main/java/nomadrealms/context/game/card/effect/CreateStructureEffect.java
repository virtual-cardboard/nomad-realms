package nomadrealms.context.game.card.effect;

import static nomadrealms.context.game.actor.structure.factory.StructureFactory.createStructure;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.structure.Structure;
import nomadrealms.context.game.actor.structure.factory.StructureType;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class CreateStructureEffect extends Effect {

	private final CardPlayer source;
	private final Tile tile;
	private final StructureType structureType;

	public CreateStructureEffect(CardPlayer source, Tile tile, StructureType structureType) {
		this.source = source;
		this.tile = tile;
		this.structureType = structureType;
	}

	@Override
	public void resolve(World world) {
		Structure newStructure = createStructure(structureType);
		newStructure.tile(tile);
		world.addActor(newStructure);
	}

}
