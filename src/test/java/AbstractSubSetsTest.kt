import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class AbstractSubSetsTest {
    private lateinit var treap: SortedSet<Int>

    protected fun fillTree(empty: SortedSet<Int>) {
        this.treap = empty
        //В произвольном порядке добавим числа от 1 до 10
        treap.add(5)
        treap.add(1)
        treap.add(2)
        treap.add(7)
        treap.add(9)
        treap.add(10)
        treap.add(8)
        treap.add(4)
        treap.add(3)
        treap.add(6)
    }

    protected fun doHeadSetTest() {
        var set: SortedSet<Int> = treap.headSet(5)
        assertEquals(true, set.contains(1))
        assertEquals(true, set.contains(2))
        assertEquals(true, set.contains(3))
        assertEquals(true, set.contains(4))
        assertEquals(false, set.contains(5))
        assertEquals(false, set.contains(6))
        assertEquals(false, set.contains(7))
        assertEquals(false, set.contains(8))
        assertEquals(false, set.contains(9))
        assertEquals(false, set.contains(10))

        set = treap.headSet(9)
        assertEquals(8, set.size)
        assertEquals(true, set.contains(1))
        assertEquals(true, set.contains(2))
        assertEquals(true, set.contains(3))
        assertEquals(true, set.contains(4))
        assertEquals(true, set.contains(5))
        assertEquals(true, set.contains(6))
        assertEquals(true, set.contains(7))
        assertEquals(true, set.contains(8))
        assertEquals(false, set.contains(9))
        assertEquals(false, set.contains(10))

        set = treap.headSet(159)
        for (i in 1..10)
            assertEquals(true, set.contains(i))
    }

    protected fun doTailSetTest() {
        var set: SortedSet<Int> = treap.tailSet(5)
        assertEquals(false, set.contains(1))
        assertEquals(false, set.contains(2))
        assertEquals(false, set.contains(3))
        assertEquals(false, set.contains(4))
        assertEquals(true, set.contains(5))
        assertEquals(true, set.contains(6))
        assertEquals(true, set.contains(7))
        assertEquals(true, set.contains(8))
        assertEquals(true, set.contains(9))
        assertEquals(true, set.contains(10))

        set = treap.tailSet(2)
        assertEquals(9, set.size)
        assertEquals(false, set.contains(1))
        assertEquals(true, set.contains(2))
        assertEquals(true, set.contains(3))
        assertEquals(true, set.contains(4))
        assertEquals(true, set.contains(5))
        assertEquals(true, set.contains(6))
        assertEquals(true, set.contains(7))
        assertEquals(true, set.contains(8))
        assertEquals(true, set.contains(9))
        assertEquals(true, set.contains(10))

        set = treap.tailSet(-236)
        for (i in 1..10)
            assertEquals(true, set.contains(i))

    }

    protected fun doHeadSetRelationTest() {
        val set: SortedSet<Int> = treap.headSet(7)
        assertEquals(6, set.size)
        assertEquals(10, treap.size)

        set.remove(4)
        assertFalse(treap.contains(4))
        assertEquals(5, set.size)
        assertEquals(9, treap.size)

        set.add(-1)
        assertTrue(set.contains(-1))
        assertTrue(treap.contains(-1))
        assertEquals(6, set.size)
        assertEquals(10, treap.size)

        treap.add(0)
        assertTrue(set.contains(0))
        assertTrue(treap.contains(0))
        assertEquals(11, treap.size)
        assertEquals(7, set.size)

        treap.remove(6)
        assertFalse(set.contains(6))
        assertEquals(10, treap.size)
        assertEquals(6, set.size)

        treap.add(12)
        assertFalse(set.contains(12))
        assertEquals(6, set.size)
        assertEquals(11, treap.size)

        set.remove(5)
        assertFalse(set.contains(5))
        assertFalse(treap.contains(5))
    }

    protected fun doTailSetRelationTest() {
        val set: SortedSet<Int> = treap.tailSet(4)
        assertEquals(7, set.size)
        assertEquals(10, treap.size)

        treap.add(12)
        assertTrue(set.contains(12))
        assertEquals(8, set.size)
        assertEquals(11, treap.size)

        set.remove(4)
        assertFalse(treap.contains(4))
        assertEquals(7, set.size)
        assertEquals(10, treap.size)

        treap.remove(6)
        assertFalse(set.contains(6))
        assertEquals(6, set.size)
        assertEquals(9, treap.size)

        treap.add(0)
        assertFalse(set.contains(0))
        assertEquals(6, set.size)
        assertEquals(10, treap.size)
    }

    protected fun doSubSetTest() {
        var set: SortedSet<Int> = treap.subSet(4, 9)
        assertEquals(5, set.size)
        assertEquals(false, set.contains(1))
        assertEquals(false, set.contains(2))
        assertEquals(false, set.contains(3))
        assertEquals(true, set.contains(4))
        assertEquals(true, set.contains(5))
        assertEquals(true, set.contains(6))
        assertEquals(true, set.contains(7))
        assertEquals(true, set.contains(8))
        assertEquals(false, set.contains(9))
        assertEquals(false, set.contains(10))

        set = treap.subSet(2, 3)
        assertEquals(1, set.size)
        assertEquals(false, set.contains(1))
        assertEquals(true, set.contains(2))
        assertEquals(false, set.contains(3))
        assertEquals(false, set.contains(4))
        assertEquals(false, set.contains(5))
        assertEquals(false, set.contains(6))
        assertEquals(false, set.contains(7))
        assertEquals(false, set.contains(8))
        assertEquals(false, set.contains(9))
        assertEquals(false, set.contains(10))
    }

    protected fun doSubSetRelationTest() {
        val set: SortedSet<Int> = treap.subSet(2, 7)
        assertEquals(5, set.size)

        set.remove(4)
        assertFalse(treap.contains(4))
        assertEquals(4, set.size)
        assertEquals(9, treap.size)

        set.add(3)
        assertTrue(set.contains(3))
        assertTrue(treap.contains(3))
        assertEquals(4, set.size)
        assertEquals(9, treap.size)

        treap.add(4)
        assertTrue(set.contains(4))
        assertTrue(treap.contains(4))
        assertEquals(10, treap.size)
        assertEquals(5, set.size)

        treap.remove(6)
        assertFalse(set.contains(6))
        assertEquals(9, treap.size)
        assertEquals(4, set.size)

        treap.add(12)
        assertFalse(set.contains(12))
        assertEquals(4, set.size)
        assertEquals(10, treap.size)

        set.remove(5)
        assertFalse(set.contains(5))
        assertFalse(treap.contains(5))
        assertEquals(3, set.size)
        assertEquals(9, treap.size)
    }
}