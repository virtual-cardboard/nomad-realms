package nomadrealms.context.game.card.query.actor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class ActorsOnTilesQuery implements Query<Actor> {

    private final Query<Tile> tilesQuery;

    public ActorsOnTilesQuery(Query<Tile> tilesQuery) {
        this.tilesQuery = tilesQuery;
    }

    @Override
    public List<Actor> find(World world, CardPlayer source) {
        return tilesQuery.find(world, source).stream()
                .map(tile -> world.getTargetOnTile(tile))
                .filter(Objects::nonNull)
                .filter(entity -> entity instanceof Actor)
                .map(entity -> (Actor) entity)
                .filter(actor -> actor != source)
                .collect(Collectors.toList());
    }

}
