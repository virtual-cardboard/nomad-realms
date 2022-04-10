package model.world.chunk.lyr2relocatepoints;

import java.util.ArrayList;
import java.util.List;

import engine.common.math.Vector2f;
import engine.common.math.Vector2i;
import model.world.chunk.AbstractTileChunk;
import model.world.chunk.lyr1randompoints.GeneratePointsChunk;
import model.world.chunk.lyr3generateActors.GenerateActorsChunk;
import model.world.chunk.lyr3generateActors.PointOfInterest;

public class RelocatePointsChunk extends GeneratePointsChunk {

	protected PointOfInterest[] relocatedPoints;

	public RelocatePointsChunk(Vector2i pos) {
		super(pos);
	}

	public static RelocatePointsChunk create(Vector2i pos, GeneratePointsChunk prev, AbstractTileChunk[][] neighbours, long worldSeed) {
		RelocatePointsChunk c = new RelocatePointsChunk(pos);

		prev.cloneDataTo(c);

		List<Vector2f> allPointPositions = new ArrayList<>(9 * NUM_NODES);
		List<Float> radii = new ArrayList<>(9 * NUM_NODES);
		for (AbstractTileChunk[] arr : neighbours) {
			for (AbstractTileChunk chunk : arr) {
				GeneratePointsChunk gnc = (GeneratePointsChunk) chunk;
				for (PointOfInterest point : gnc.points()) {
					Vector2f chunkPosDiff = chunk.pos().sub(pos).toVec2f().scale(CHUNK_SIDE_LENGTH);
					allPointPositions.add(point.pos().add(chunkPosDiff));
					radii.add(point.radius());
				}
			}
		}

		// Movement vectors that will be added to the point positions
		Vector2f[] moveVecs = new Vector2f[c.points.length];
		for (int i = 0; i < moveVecs.length; i++) {
			moveVecs[i] = new Vector2f();
		}

		// Edit movement vectors
		for (int i = 0; i < c.points.length; i++) {
			PointOfInterest p = c.points[i];
			Vector2f pPos = p.pos();
			for (int j = 0; j < allPointPositions.size(); j++) {
				Vector2f o = allPointPositions.get(j);
				if (!p.pos().equals(o)) {
					Vector2f oToP = pPos.sub(o);
					float dist = oToP.length();
					Vector2f toAdd = oToP.normalise().scale(5 * radii.get(j) / (p.radius() * dist * dist + dist + 4.15f));
					moveVecs[i] = moveVecs[i].add(toAdd);
				}
			}
		}

		// Create list of relocated nodes
		c.relocatedPoints = new PointOfInterest[c.points.length];
		for (int i = 0; i < c.points.length; i++) {
			PointOfInterest p = c.points[i];
			c.relocatedPoints[i] = new PointOfInterest(p.pos().add(moveVecs[i]), c.points[i].radius());
		}
		return c;
	}

	@Override
	public int layer() {
		return 2;
	}

	@Override
	public GenerateActorsChunk upgrade(AbstractTileChunk[][] neighbours, long worldSeed) {
		return GenerateActorsChunk.create(pos(), this, neighbours, worldSeed);
	}

	public <T extends RelocatePointsChunk> void cloneDataTo(T c) {
		c.relocatedPoints = relocatedPoints;
		super.cloneDataTo((GeneratePointsChunk) c);
	}

	public PointOfInterest[] relocatedNodes() {
		return relocatedPoints;
	}

}
