import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JavaHeadSetTest : AbstractHeadTailTest() {

    @BeforeEach
    fun fillTree() {
        fillTree(Treap<Int>())
    }

    @Test
    fun headSetTest() {
        doHeadSetTest()
    }

    @Test
    fun headSetRelationTest() {
        doHeadSetRelationTest()
    }

    @Test
    fun tailSetTest() {
        doTailSetTest()
    }

    @Test
    fun tailSetRelationTest() {
        doTailSetRelationTest()
    }

    @Test
    fun subSetTest() {
        doSubSetTest()
    }

    @Test
    fun subSetRelationTest() {
        doSubSetRelationTest()
    }
}