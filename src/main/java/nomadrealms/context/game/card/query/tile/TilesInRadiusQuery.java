package nomadrealms.context.game.card.query.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import nomadrealms.context.game.actor.HasPosition;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;

public class TilesInRadiusQuery implements Query<Tile> {

    private final Query<? extends HasPosition> centerQuery;
    private final int radius;

    public TilesInRadiusQuery(Query<? extends HasPosition> centerQuery, int radius) {
        this.centerQuery = centerQuery;
        this.radius = radius;
    }

    @Override
    public List<Tile> find(World world, CardPlayer source) {
        List<? extends HasPosition> centers = centerQuery.find(world, source);
        if (centers.isEmpty()) {
            return Collections.emptyList();
        }

        Tile centerTile = centers.get(0).tile();
        if (centerTile == null) {
            return Collections.emptyList();
        }

        Set<Tile> visited = new HashSet<>();
        List<Tile> frontier = new ArrayList<>();
        List<Tile> tilesInRadius = new ArrayList<>();

        frontier.add(centerTile);
        visited.add(centerTile);
        tilesInRadius.add(centerTile);

        for (int i = 0; i < radius; i++) {
            List<Tile> nextFrontier = new ArrayList<>();
            for (Tile tile : frontier) {
                addNeighbor(world, nextFrontier, visited, tilesInRadius, tile.coord().ul());
                addNeighbor(world, nextFrontier, visited, tilesInRadius, tile.coord().um());
                addNeighbor(world, nextFrontier, visited, tilesInRadius, tile.coord().ur());
                addNeighbor(world, nextFrontier, visited, tilesInRadius, tile.coord().dl());
                addNeighbor(world, nextFrontier, visited, tilesInRadius, tile.coord().dm());
                addNeighbor(world, nextFrontier, visited, tilesInRadius, tile.coord().dr());
            }
            frontier = nextFrontier;
        }

        return tilesInRadius;
    }

    private void addNeighbor(World world, List<Tile> nextFrontier, Set<Tile> visited, List<Tile> tilesInRadius, TileCoordinate neighborCoord) {
        Tile neighbor = world.getTile(neighborCoord);
        if (neighbor != null && !visited.contains(neighbor)) {
            visited.add(neighbor);
            nextFrontier.add(neighbor);
            tilesInRadius.add(neighbor);
        }
    }
}
