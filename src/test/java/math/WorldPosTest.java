package math;

import static org.junit.Assert.assertEquals;

import engine.common.math.Vector2i;
import org.junit.Test;

public class WorldPosTest {

	@Test
	public void testDistanceTo() {
		{
			WorldPos p1 = new WorldPos();
			WorldPos p2 = new WorldPos();
			assertEquals(p1.distanceTo(p2), 0);
			assertEquals(p2.distanceTo(p1), 0);
		}
		{
			WorldPos p1 = new WorldPos(0, 0, 1, 1);
			WorldPos p2 = new WorldPos(0, 0, 3, 4);
			assertEquals(p1.distanceTo(p2), 4);
			assertEquals(p2.distanceTo(p1), 4);
		}
		{
			WorldPos p1 = new WorldPos(0, 0, 1, 1);
			WorldPos p2 = new WorldPos(0, 0, 4, 4);
			assertEquals(p1.distanceTo(p2), 4);
			assertEquals(p2.distanceTo(p1), 4);
		}
		{
			WorldPos p1 = new WorldPos(0, 0, 2, 1);
			WorldPos p2 = new WorldPos(0, 0, 5, 0);
			assertEquals(p1.distanceTo(p2), 3);
			assertEquals(p2.distanceTo(p1), 3);
		}
		{
			WorldPos p1 = new WorldPos(0, 0, 2, 9);
			WorldPos p2 = new WorldPos(0, 0, 5, 0);
			assertEquals(p1.distanceTo(p2), 10);
			assertEquals(p2.distanceTo(p1), 10);
		}
		{
			WorldPos p1 = new WorldPos(0, 0, 2, 9);
			WorldPos p2 = new WorldPos(0, 0, 6, 5);
			assertEquals(p1.distanceTo(p2), 6);
			assertEquals(p2.distanceTo(p1), 6);
		}
		{
			WorldPos p1 = new WorldPos(0, 0, 6, 5);
			WorldPos p2 = new WorldPos(0, 0, 1, 3);
			assertEquals(p1.distanceTo(p2), 5);
			assertEquals(p2.distanceTo(p1), 5);
		}
		{
			WorldPos p1 = new WorldPos(0, 0, 5, 3);
			WorldPos p2 = new WorldPos(0, 0, 3, 8);
			assertEquals(p1.distanceTo(p2), 6);
			assertEquals(p2.distanceTo(p1), 6);
		}
		{
			WorldPos p1 = new WorldPos(0, 0, 5, 3);
			WorldPos p2 = new WorldPos(0, 0, 3, 8);
			assertEquals(p1.distanceTo(p2), 6);
			assertEquals(p2.distanceTo(p1), 6);
		}
		{
			WorldPos p1 = new WorldPos(0, 0, 2, 3);
			WorldPos p2 = new WorldPos(0, 0, 4, 9);
			assertEquals(p1.distanceTo(p2), 7);
			assertEquals(p2.distanceTo(p1), 7);
		}
	}

	@Test
	public void testDirectionTo() {
		{
			WorldPos p1 = new WorldPos(0, 0, 2, 4);
			WorldPos p2 = new WorldPos(0, 0, 5, 3);
			Vector2i directionTo = p1.directionTo(p2);
			assertEquals(directionTo.x(), 1);
			assertEquals(directionTo.y(), -1);
		}
		{
			WorldPos p1 = new WorldPos(0, 0, 1, 4);
			WorldPos p2 = new WorldPos(0, 0, 5, 3);
			Vector2i directionTo = p1.directionTo(p2);
			assertEquals(directionTo.x(), 1);
			assertEquals(directionTo.y(), 0);
		}
		{
			WorldPos p1 = new WorldPos(0, 0, 1, 4);
			WorldPos p2 = new WorldPos(0, 0, 6, 8);
			Vector2i directionTo = p1.directionTo(p2);
			assertEquals(directionTo.x(), 1);
			assertEquals(directionTo.y(), 1);
		}
	}

}
