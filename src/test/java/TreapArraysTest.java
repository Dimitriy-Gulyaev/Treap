import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

// Здесь находятся тесты всех All-методов и каст к массивам. Код понятен интуитивно
class TreapArraysTest {

    @Test
    void testToArray() {
        Treap<Integer> treap = new Treap<>();
        treap.add(1);
        treap.add(2);
        treap.add(3);
        assertArrayEquals(new Object[]{1, 2, 3}, treap.toArray());
        Object[] integers = treap.toArray(new Integer[3]);
        assertArrayEquals(integers, new Integer[]{1, 2, 3});
    }

    @Test
    void testRemoveAll() {
        Treap<Integer> treap = new Treap<>();
        treap.add(1);
        treap.add(2);
        treap.add(3);
        ArrayList<Integer> toRemove = new ArrayList<>();
        toRemove.add(2);
        toRemove.add(3);
        treap.removeAll(toRemove);
        assertArrayEquals(treap.toArray(), new Object[]{1});
    }

    @Test
    void testAddAll() {
        Treap<Integer> treap = new Treap<>();
        treap.add(1);
        treap.add(2);
        treap.add(3);
        ArrayList<Integer> toAdd = new ArrayList<>();
        toAdd.add(4);
        toAdd.add(5);
        treap.addAll(toAdd);
        assertArrayEquals(treap.toArray(), new Object[]{1, 2, 3, 4, 5});
    }

    @Test
    void testRetainAll() {
        Treap<Integer> treap = new Treap<>();
        treap.add(1);
        treap.add(2);
        treap.add(3);
        ArrayList<Integer> toRetain = new ArrayList<>();
        toRetain.add(2);
        toRetain.add(3);
        treap.retainAll(toRetain);
        assertArrayEquals(treap.toArray(), new Object[]{2, 3});
    }
}
