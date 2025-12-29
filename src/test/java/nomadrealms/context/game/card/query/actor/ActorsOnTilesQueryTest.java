package nomadrealms.context.game.card.query.actor;

import org.junit.jupiter.api.Test;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

class ActorsOnTilesQueryTest {

    @Test
    void testFind() {
        World world = mock(World.class);
        CardPlayer source = mock(CardPlayer.class);
        Tile tile1 = mock(Tile.class);
        Tile tile2 = mock(Tile.class);
        Actor actor1 = mock(Actor.class);
        Actor actor2 = mock(Actor.class);

        Query<Tile> tilesQuery = (w, s) -> List.of(tile1, tile2);

        when(world.getTargetOnTile(tile1)).thenReturn(actor1);
        when(world.getTargetOnTile(tile2)).thenReturn(actor2);

        ActorsOnTilesQuery query = new ActorsOnTilesQuery(tilesQuery);
        List<Actor> result = query.find(world, source);

        assertEquals(2, result.size());
        assertEquals(actor1, result.get(0));
        assertEquals(actor2, result.get(1));
    }

    @Test
    void testFind_excludesSource() {
        World world = mock(World.class);
        CardPlayer source = mock(CardPlayer.class);
        Tile tile1 = mock(Tile.class);
        Tile tile2 = mock(Tile.class);
        Actor actor1 = mock(Actor.class);

        Query<Tile> tilesQuery = (w, s) -> List.of(tile1, tile2);

        when(world.getTargetOnTile(tile1)).thenReturn(actor1);
        when(world.getTargetOnTile(tile2)).thenReturn(source);

        ActorsOnTilesQuery query = new ActorsOnTilesQuery(tilesQuery);
        List<Actor> result = query.find(world, source);

        assertEquals(1, result.size());
        assertEquals(actor1, result.get(0));
    }
}
