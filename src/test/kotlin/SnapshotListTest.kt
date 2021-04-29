import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SnapshotListTest {

    @Test
    fun `empty list has size zero`() {
        assertEquals(0, SnapshotList().size)
    }

    @Test
    fun `addFirst works`() {
        val list = SnapshotList()

        list.addFirst("hello")

        assertEquals(1, list.size)
        assertTrue(list.contains("hello"))
    }

    @Test
    fun `removeLast works`() {
        val list = SnapshotList()
        list.addFirst("hello")

        val last = list.removeLast()

        assertEquals("hello", last)
        assertEquals(0, list.size)
        assertFalse(list.contains("hello"))
    }

    @Test fun `list can hold multiple items`() {
        val list = SnapshotList()

        list.addFirst("one")
        list.addFirst("two")
        list.addFirst("three")

        assertEquals(3, list.size)
        assertTrue(list.contains("one"))
        assertTrue(list.contains("two"))
        assertTrue(list.contains("three"))
    }

    @Test fun `list adds and removes are ordered`() {
        val list = SnapshotList()

        list.addFirst("one")
        list.addFirst("two")
        list.addFirst("three")
        val first = list.removeLast()
        val second = list.removeLast()
        val third = list.removeLast()

        assertEquals("one", first)
        assertEquals("two", second)
        assertEquals("three", third)
    }
}
