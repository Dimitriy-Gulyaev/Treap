import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

final class TreapBasicFunctionsTest {

    @Test
    void testAdd() {
        Treap<Integer> treap = new Treap<>();
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
        treap.add(8);
        treap.add(15);
        treap.add(15);
        treap.add(20);
        assertEquals(9, treap.size());
        assertTrue(treap.contains(8));
        assertTrue(treap.checkInvariant());
        assertTrue(treap.checkPriorityOrder());
        assertEquals((Integer) 1, treap.first());
        assertEquals((Integer) 20, treap.last());
        assertFalse(treap.remove(37));
        assertFalse(treap.remove(null));
        assertFalse(treap.add(null));
    }

    @Test
    void testRemove() {
        Random random = new Random();
        for (int i = 1; i <= 100; i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 1; j <= 20; j++) {
                list.add(random.nextInt(100));
            }

            SortedSet<Integer> treeSet = new TreeSet<>();
            Treap<Integer> treap = new Treap<>();
            for (Integer element : list) {
                treeSet.add(element);
                treap.add(element);
            }

            Integer toRemove = list.get(random.nextInt(list.size()));
            treeSet.remove(toRemove);
            treap.remove(toRemove);

            System.out.println("Removing " + toRemove.toString() + " from " + list.toString());
            assertEquals(treeSet.size(), treap.size());
            for (Integer element : list) {
                boolean inn = !element.equals(toRemove);
                assertEquals(inn, treap.contains(element),
                        inn ? element.toString() + "should be in treap" :
                                element.toString() + " shouldn't be in treap");
            }
            assertTrue(treap.checkInvariant());
            assertTrue(treap.checkPriorityOrder());
        }
    }

    @Test
    void testIterator() {
        Random random = new Random();
        for (int i = 1; i <= 100; i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 1; j <= 20; j++) {
                list.add(random.nextInt(100));
            }

            SortedSet<Integer> treeSet = new TreeSet<>();
            Treap<Integer> treap = new Treap<>();
            for (Integer element : list) {
                treeSet.add(element);
                treap.add(element);
            }
            Iterator<Integer> treeIt = treeSet.iterator();
            Iterator<Integer> treapIt = treap.iterator();
            System.out.println("Traversing" + list.toString());
            while (treeIt.hasNext()) {
                assertEquals(treeIt.next(), treapIt.next());
            }
        }
    }
}
