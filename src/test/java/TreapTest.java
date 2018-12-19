import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreapTest {

    @SuppressWarnings("unchecked")
    @Test
    void testTreapBasicOperations() {
        Treap treap = new Treap<Integer>();
        treap.add(10);
        treap.add(5);
        treap.add(7);
        treap.add(10);
        assertEquals(3, treap.size());
        assertTrue(treap.contains(5));
        treap.add(3);
        treap.add(1);
        treap.add(3);
        treap.add(4);
        assertEquals(6, treap.size());
        assertFalse(treap.contains(8));
        assertFalse(treap.add(null));
        treap.add(8);
        treap.add(15);
        treap.add(15);
        treap.add(20);
        assertEquals(9, treap.size());
        assertTrue(treap.contains(8));
        assertTrue(treap.checkInvariant());
        assertTrue(treap.checkPriorityOrder());
        assertEquals(1, treap.first());
        assertEquals(20, treap.last());
        assertFalse(treap.remove(123123));
        assertFalse(treap.remove(null));
        assertTrue(treap.remove(7));
        assertFalse(treap.contains(7));
        assertFalse(treap.remove(7));
        assertEquals(8, treap.size());
        assertTrue(treap.checkInvariant());
        assertTrue(treap.checkPriorityOrder());
        treap.remove(1);
        treap.remove(3);
        treap.remove(15);
        treap.remove(20);
        assertTrue(treap.checkInvariant());
        assertTrue(treap.checkPriorityOrder());
    }
}
