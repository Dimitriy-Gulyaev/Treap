import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.*;

class SubSetTest {
    private Treap<Integer> treap;

    @BeforeEach
    void fillTree() {
        this.treap = new Treap<>();
        //В произвольном порядке добавим числа от 1 до 10
        treap.add(5);
        treap.add(1);
        treap.add(2);
        treap.add(7);
        treap.add(9);
        treap.add(10);
        treap.add(8);
        treap.add(4);
        treap.add(3);
        treap.add(6);
    }

    @Test
    void doHeadSetTest() {
        SortedSet<Integer> headSet = treap.headSet(5);
        assertTrue(headSet.contains(1));
        assertTrue(headSet.contains(2));
        assertTrue(headSet.contains(3));
        assertTrue(headSet.contains(4));
        assertFalse(headSet.contains(5));
        assertFalse(headSet.contains(6));
        assertFalse(headSet.contains(7));
        assertFalse(headSet.contains(8));
        assertFalse(headSet.contains(9));
        assertFalse(headSet.contains(10));

        headSet = treap.headSet(9);
        assertEquals(8, headSet.size());
        assertTrue(headSet.contains(1));
        assertTrue(headSet.contains(2));
        assertTrue(headSet.contains(3));
        assertTrue(headSet.contains(4));
        assertTrue(headSet.contains(5));
        assertTrue(headSet.contains(6));
        assertTrue(headSet.contains(7));
        assertTrue(headSet.contains(8));
        assertFalse(headSet.contains(9));
        assertFalse(headSet.contains(10));

        headSet = treap.headSet(159);
        for (int i = 1; i <= 10; i++)
            assertTrue(headSet.contains(i));
    }

    @Test
    void doHeadSetRelationTest() {
        SortedSet<Integer> headSet = treap.headSet(7);
        assertEquals(6, headSet.size());
        assertEquals(10, treap.size());

        headSet.remove(4);
        assertFalse(treap.contains(4));
        assertEquals(5, headSet.size());
        assertEquals(9, treap.size());

        headSet.add(-1);
        assertTrue(headSet.contains(-1));
        assertTrue(treap.contains(-1));
        assertEquals(6, headSet.size());
        assertEquals(10, treap.size());

        treap.add(0);
        assertTrue(headSet.contains(0));
        assertTrue(treap.contains(0));
        assertEquals(11, treap.size());
        assertEquals(7, headSet.size());

        treap.remove(6);
        assertFalse(headSet.contains(6));
        assertEquals(10, treap.size());
        assertEquals(6, headSet.size());

        treap.add(12);
        assertFalse(headSet.contains(12));
        assertEquals(6, headSet.size());
        assertEquals(11, treap.size());

        headSet.remove(5);
        assertFalse(headSet.contains(5));
        assertFalse(treap.contains(5));
    }

    @Test
    void doTailSetTest() {
        SortedSet<Integer> tailSet = treap.tailSet(5);
        assertFalse(tailSet.contains(1));
        assertFalse(tailSet.contains(2));
        assertFalse(tailSet.contains(3));
        assertFalse(tailSet.contains(4));
        assertTrue(tailSet.contains(5));
        assertTrue(tailSet.contains(6));
        assertTrue(tailSet.contains(7));
        assertTrue(tailSet.contains(8));
        assertTrue(tailSet.contains(9));
        assertTrue(tailSet.contains(10));

        tailSet = treap.tailSet(2);
        assertEquals(9, tailSet.size());
        assertFalse(tailSet.contains(1));
        assertTrue(tailSet.contains(2));
        assertTrue(tailSet.contains(3));
        assertTrue(tailSet.contains(4));
        assertTrue(tailSet.contains(5));
        assertTrue(tailSet.contains(6));
        assertTrue(tailSet.contains(7));
        assertTrue(tailSet.contains(8));
        assertTrue(tailSet.contains(9));
        assertTrue(tailSet.contains(10));

        tailSet = treap.tailSet(-236);
        for (int i = 1; i <= 10; i++)
            assertTrue(tailSet.contains(i));

    }

    @Test
    void doTailSetRelationTest() {
        SortedSet<Integer> tailSet = treap.tailSet(4);
        assertEquals(7, tailSet.size());
        assertEquals(10, treap.size());

        treap.add(12);
        assertTrue(tailSet.contains(12));
        assertEquals(8, tailSet.size());
        assertEquals(11, treap.size());

        tailSet.remove(4);
        assertFalse(treap.contains(4));
        assertEquals(7, tailSet.size());
        assertEquals(10, treap.size());

        treap.remove(6);
        assertFalse(tailSet.contains(6));
        assertEquals(6, tailSet.size());
        assertEquals(9, treap.size());

        treap.add(0);
        assertFalse(tailSet.contains(0));
        assertEquals(6, tailSet.size());
        assertEquals(10, treap.size());
    }

    @Test
    void doSubSetTest() {
        SortedSet<Integer> subSet = treap.subSet(4, 9);
        assertEquals(5, subSet.size());
        assertFalse(subSet.contains(1));
        assertFalse(subSet.contains(2));
        assertFalse(subSet.contains(3));
        assertTrue(subSet.contains(4));
        assertTrue(subSet.contains(5));
        assertTrue(subSet.contains(6));
        assertTrue(subSet.contains(7));
        assertTrue(subSet.contains(8));
        assertFalse(subSet.contains(9));
        assertFalse(subSet.contains(10));

        subSet = treap.subSet(2, 3);
        assertEquals(1, subSet.size());
        assertFalse(subSet.contains(1));
        assertTrue(subSet.contains(2));
        assertFalse(subSet.contains(3));
        assertFalse(subSet.contains(4));
        assertFalse(subSet.contains(5));
        assertFalse(subSet.contains(6));
        assertFalse(subSet.contains(7));
        assertFalse(subSet.contains(8));
        assertFalse(subSet.contains(9));
        assertFalse(subSet.contains(10));
    }

    @Test
    void doSubSetRelationTest() {
        SortedSet<Integer> subSet = treap.subSet(2, 7);
        assertEquals(5, subSet.size());

        subSet.remove(4);
        assertFalse(treap.contains(4));
        assertEquals(4, subSet.size());
        assertEquals(9, treap.size());

        subSet.add(3);
        assertTrue(subSet.contains(3));
        assertTrue(treap.contains(3));
        assertEquals(4, subSet.size());
        assertEquals(9, treap.size());

        treap.add(4);
        assertTrue(subSet.contains(4));
        assertTrue(treap.contains(4));
        assertEquals(10, treap.size());
        assertEquals(5, subSet.size());

        treap.remove(6);
        assertFalse(subSet.contains(6));
        assertEquals(9, treap.size());
        assertEquals(4, subSet.size());

        treap.add(12);
        assertFalse(subSet.contains(12));
        assertEquals(4, subSet.size());
        assertEquals(10, treap.size());

        subSet.remove(5);
        assertFalse(subSet.contains(5));
        assertFalse(treap.contains(5));
        assertEquals(3, subSet.size());
        assertEquals(9, treap.size());
    }
}
