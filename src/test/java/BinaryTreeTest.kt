import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.*

class BinaryTreeTest {
    private fun testAdd(create: () -> Treap<Int>) {
        val tree = create()
        tree.add(10)
        tree.add(5)
        tree.add(7)
        tree.add(10)
        assertEquals(3, tree.size)
        assertTrue(tree.contains(5))
        tree.add(3)
        tree.add(1)
        tree.add(3)
        tree.add(4)
        assertEquals(6, tree.size)
        assertFalse(tree.contains(8))
        tree.add(8)
        tree.add(15)
        tree.add(15)
        tree.add(20)
        assertEquals(9, tree.size)
        assertTrue(tree.contains(8))
        assertTrue(tree.checkInvariant())
        assertEquals(1, tree.first())
        assertEquals(20, tree.last())
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
            val binarySet = create()
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val toRemove = list[random.nextInt(list.size)]
            treeSet.remove(toRemove)

            println("Removing $toRemove from $list")
            assertEquals(treeSet.size, binarySet.size)
            for (element in list) {
                val inn = element != toRemove
                assertEquals(inn, element in binarySet,
                        "$element should be ${if (inn) "in" else "not in"} tree")
            }
            assertTrue(binarySet.checkInvariant())
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
            val binarySet = create()
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val treeIt = treeSet.iterator()
            val binaryIt = binarySet.iterator()
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

//    private fun testIteratorRemove(create: () -> SortedSet<Int>) {
//        val random = Random()
//        for (iteration in 1..100) {
//            val list = mutableListOf<Int>()
//            for (i in 1..20) {
//                list.add(random.nextInt(100))
//            }
//
//            val treeSet = TreeSet<Int>()
//            val binarySet = create()
//            for (element in list) {
//                treeSet += element
//                binarySet += element
//            }
//
//            val toRemove = list[random.nextInt(list.size)]
//            treeSet.remove(toRemove)
//            println("Removing $toRemove from $list")
//            val iterator = binarySet.iterator()
//            while (iterator.hasNext()) {
//                val element = iterator.next()
//                print("$element ")
//                if (element == toRemove) {
//                    iterator.remove()
//                }
//            }
//            println()
//            assertEquals<SortedSet<*>>(treeSet, binarySet, "After removal of $toRemove from $list")
//            assertEquals(treeSet.size, binarySet.size)
//            for (element in list) {
//                val inn = element != toRemove
//                assertEquals(inn, element in binarySet,
//                        "$element should be ${if (inn) "in" else "not in"} tree")
//            }
//            assertTrue(binarySet.checkInvariant())
//        }
//    }
//
//    @Test
//    fun testIteratorRemoveJava() {
//        testIteratorRemove { createJavaTree() }
//    }
}
