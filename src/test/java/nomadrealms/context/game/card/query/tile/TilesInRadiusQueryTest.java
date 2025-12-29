package nomadrealms.context.game.card.query.tile;

import org.junit.jupiter.api.Test;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.actor.SelfQuery;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

class TilesInRadiusQueryTest {

    @Test
    void testFind() {
        World world = mock(World.class);
        CardPlayer source = mock(CardPlayer.class);
        Tile centerTile = mock(Tile.class);
        TileCoordinate centerCoord = mock(TileCoordinate.class);

        when(source.tile()).thenReturn(centerTile);
        when(centerTile.coord()).thenReturn(centerCoord);

        Tile ul = mock(Tile.class);
        Tile um = mock(Tile.class);
        Tile ur = mock(Tile.class);
        Tile dl = mock(Tile.class);
        Tile dm = mock(Tile.class);
        Tile dr = mock(Tile.class);

        TileCoordinate ulCoord = mock(TileCoordinate.class);
        TileCoordinate umCoord = mock(TileCoordinate.class);
        TileCoordinate urCoord = mock(TileCoordinate.class);
        TileCoordinate dlCoord = mock(TileCoordinate.class);
        TileCoordinate dmCoord = mock(TileCoordinate.class);
        TileCoordinate drCoord = mock(TileCoordinate.class);

        when(centerCoord.ul()).thenReturn(ulCoord);
        when(centerCoord.um()).thenReturn(umCoord);
        when(centerCoord.ur()).thenReturn(urCoord);
        when(centerCoord.dl()).thenReturn(dlCoord);
        when(centerCoord.dm()).thenReturn(dmCoord);
        when(centerCoord.dr()).thenReturn(drCoord);

        when(world.getTile(ulCoord)).thenReturn(ul);
        when(world.getTile(umCoord)).thenReturn(um);
        when(world.getTile(urCoord)).thenReturn(ur);
        when(world.getTile(dlCoord)).thenReturn(dl);
        when(world.getTile(dmCoord)).thenReturn(dm);
        when(world.getTile(drCoord)).thenReturn(dr);

        TilesInRadiusQuery query = new TilesInRadiusQuery(new SelfQuery(), 1);
        List<Tile> result = query.find(world, source);

        assertEquals(7, result.size());
    }
}
