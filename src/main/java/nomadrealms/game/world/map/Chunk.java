//package nomadrealms.game.world.map;
//
//import common.math.Vector2i;
//import nomadrealms.game.world.map.generation.MapGenerationStrategy;
//import nomadrealms.game.world.map.area.Tile;
//import nomadrealms.render.RenderingEnvironment;
//
//public class Chunk {
//
//	public static int CHUNK_SIZE = 16;
//
//	private final Vector2i coord;
//	private Tile[][] tiles;
//
//	public Chunk(Vector2i coord, MapGenerationStrategy strategy) {
//		this.coord = coord;
//		tiles = strategy.generate(this, coord);
//	}
//
//	public void render(RenderingEnvironment renderingEnvironment) {
//		for (int row = 0; row < CHUNK_SIZE; row++) {
//			for (int col = 0; col < CHUNK_SIZE; col++) {
//				tiles[row][col].render(renderingEnvironment);
//			}
//		}
//	}
//
//	public Tile getTile(int col, int row) {
//		if (row < 0 || row >= CHUNK_SIZE || col < 0 || col >= CHUNK_SIZE) {
//			return null;
//		}
//		return tiles[row][col];
//	}
//
//    public void setTile(Tile tile) {
//		tiles[tile.y()][tile.x()] = tile;
//    }
//
//	public int x() {
//		return coord.x();
//	}
//
//	public int y() {
//		return coord.y();
//	}
//}
