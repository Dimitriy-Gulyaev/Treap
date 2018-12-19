import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class AbstractHeadTailTest {
    private lateinit var tree: SortedSet<Int>

    protected fun fillTree(empty: SortedSet<Int>) {
        this.tree = empty
        //В произвольном порядке добавим числа от 1 до 10
        tree.add(5)
        tree.add(1)
        tree.add(2)
        tree.add(7)
        tree.add(9)
        tree.add(10)
        tree.add(8)
        tree.add(4)
        tree.add(3)
        tree.add(6)
    }

    protected fun doHeadSetTest() {
        var set: SortedSet<Int> = tree.headSet(5)
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

        set = tree.headSet(9)
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

        set = tree.headSet(159)
        for (i in 1..10)
            assertEquals(true, set.contains(i))
    }

    protected fun doTailSetTest() {
        var set: SortedSet<Int> = tree.tailSet(5)
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

        set = tree.tailSet(2)
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

        set = tree.tailSet(-236)
        for (i in 1..10)
            assertEquals(true, set.contains(i))

    }

    protected fun doHeadSetRelationTest() {
        val set: SortedSet<Int> = tree.headSet(7)
        assertEquals(6, set.size)
        assertEquals(10, tree.size)

        set.remove(4)
        assertFalse(tree.contains(4))
        assertEquals(5, set.size)
        assertEquals(9, tree.size)

        set.add(-1)
        assertTrue(set.contains(-1))
        assertTrue(tree.contains(-1))
        assertEquals(6, set.size)
        assertEquals(10, tree.size)

        tree.add(0)
        assertTrue(set.contains(0))
        assertTrue(tree.contains(0))
        assertEquals(11, tree.size)
        assertEquals(7, set.size)

        tree.remove(6)
        assertFalse(set.contains(6))
        assertEquals(10, tree.size)
        assertEquals(6, set.size)

        tree.add(12)
        assertFalse(set.contains(12))
        assertEquals(6, set.size)
        assertEquals(11, tree.size)

        set.remove(5)
        assertFalse(set.contains(5))
        assertFalse(tree.contains(5))
    }

    protected fun doTailSetRelationTest() {
        val set: SortedSet<Int> = tree.tailSet(4)
        assertEquals(7, set.size)
        assertEquals(10, tree.size)

        tree.add(12)
        assertTrue(set.contains(12))
        assertEquals(8, set.size)
        assertEquals(11, tree.size)

        set.remove(4)
        assertFalse(tree.contains(4))
        assertEquals(7, set.size)
        assertEquals(10, tree.size)

        tree.remove(6)
        assertFalse(set.contains(6))
        assertEquals(6, set.size)
        assertEquals(9, tree.size)

        tree.add(0)
        assertFalse(set.contains(0))
        assertEquals(6, set.size)
        assertEquals(10, tree.size)
    }

    protected fun doSubSetTest() {
        var set: SortedSet<Int> = tree.subSet(4, 9)
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

        set = tree.subSet(2, 3)
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
        val set: SortedSet<Int> = tree.subSet(2, 7)
        assertEquals(5, set.size)

        set.remove(4)
        assertFalse(tree.contains(4))
        assertEquals(4, set.size)
        assertEquals(9, tree.size)

        set.add(3)
        assertTrue(set.contains(3))
        assertTrue(tree.contains(3))
        assertEquals(4, set.size)
        assertEquals(9, tree.size)

        tree.add(4)
        assertTrue(set.contains(4))
        assertTrue(tree.contains(4))
        assertEquals(10, tree.size)
        assertEquals(5, set.size)

        tree.remove(6)
        assertFalse(set.contains(6))
        assertEquals(9, tree.size)
        assertEquals(4, set.size)

        tree.add(12)
        assertFalse(set.contains(12))
        assertEquals(4, set.size)
        assertEquals(10, tree.size)

        set.remove(5)
        assertFalse(set.contains(5))
        assertFalse(tree.contains(5))
        assertEquals(3, set.size)
        assertEquals(9, tree.size)
    }
}