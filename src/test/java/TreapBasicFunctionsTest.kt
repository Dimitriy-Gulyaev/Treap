import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.*

class TreapBasicFunctionsTest {
    private fun testAdd(create: () -> Treap<Int>) {
        val treap = create()
        treap.add(10)
        treap.add(5)
        treap.add(7)
        treap.add(10)
        assertEquals(3, treap.size)
        assertTrue(treap.contains(5))
        treap.add(3)
        treap.add(1)
        treap.add(3)
        treap.add(4)
        assertEquals(6, treap.size)
        assertFalse(treap.contains(8))
        treap.add(8)
        treap.add(15)
        treap.add(15)
        treap.add(20)
        assertEquals(9, treap.size)
        assertTrue(treap.contains(8))
        assertTrue(treap.checkInvariant())
        assertTrue(treap.checkPriorityOrder())
        assertEquals(1, treap.first())
        assertEquals(20, treap.last())
    }

    @Test
    fun testAddJava() {
        testAdd { createJavaTree() }
    }

    private fun <T : Comparable<T>> createJavaTree(): Treap<T> = Treap()


    private fun testRemove(create: () -> Treap<Int>) {
        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }

            val treeSet = TreeSet<Int>()
            val treap = create()
            for (element in list) {
                treeSet += element
                treap += element
            }
            val toRemove = list[random.nextInt(list.size)]
            treeSet.remove(toRemove)
            treap.remove(toRemove)

            println("Removing $toRemove from $list")
            assertEquals(treeSet.size, treap.size)
            for (element in list) {
                val inn = element != toRemove
                assertEquals(inn, element in treap,
                        "$element should be ${if (inn) "in" else "not in"} tree")
            }
            assertTrue(treap.checkInvariant())
            assertTrue(treap.checkPriorityOrder())
        }
    }

    @Test
    fun testRemoveJava() {
        testRemove { createJavaTree() }
    }

    private fun testIterator(create: () -> SortedSet<Int>) {
        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }

            val treeSet = TreeSet<Int>()
            val treap = create()
            for (element in list) {
                treeSet += element
                treap += element
            }
            val treeIt = treeSet.iterator()
            val binaryIt = treap.iterator()
            println("Traversing $list")
            while (treeIt.hasNext()) {
                assertEquals(treeIt.next(), binaryIt.next())
            }
        }
    }

    @Test
    fun testIteratorJava() {
        testIterator { createJavaTree() }
    }
}
