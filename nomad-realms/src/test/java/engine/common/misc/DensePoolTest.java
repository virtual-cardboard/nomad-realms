package engine.common.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DensePoolTest {

	@Test
	public void testAddAndCapacity() {
		DensePool<String> pool = new DensePool<>(3);
		assertEquals(0, pool.size());
		assertEquals(3, pool.capacity());

		assertTrue(pool.add("item1", 1000L));
		assertTrue(pool.add("item2", 2000L));
		assertTrue(pool.add("item3", 3000L));

		assertEquals(3, pool.size());
		assertFalse(pool.add("item4", 4000L)); // full
		assertFalse(pool.add(null, 5000L));    // null item
		assertEquals(3, pool.size());

		assertEquals("item1", pool.get(0));
		assertEquals(1000L, pool.getCreationTime(0));
		assertEquals("item2", pool.get(1));
		assertEquals("item3", pool.get(2));
	}

	@Test
	public void testSwapAndPopRemoval() {
		DensePool<String> pool = new DensePool<>(10);
		pool.add("A", 100L);
		pool.add("B", 200L);
		pool.add("C", 300L);
		pool.add("D", 400L);

		assertEquals(4, pool.size());

		List<String> processed = new ArrayList<>();
		// Expire "B" and "C" (keep "A" and "D")
		pool.process((item, time) -> !item.equals("B") && !item.equals("C"), processed::add);

		assertEquals(2, pool.size());
		assertTrue(processed.contains("A"));
		assertTrue(processed.contains("D"));
		assertFalse(processed.contains("B"));
		assertFalse(processed.contains("C"));
	}

	@Test
	public void testClear() {
		DensePool<String> pool = new DensePool<>(5);
		pool.add("A");
		pool.add("B");
		assertEquals(2, pool.size());

		pool.clear();
		assertEquals(0, pool.size());
	}

}
