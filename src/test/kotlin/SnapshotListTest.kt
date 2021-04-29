import org.junit.Test
import kotlin.test.*

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

    @Test
    fun `list can hold lots of stuff`() {
        val list = SnapshotList()

        repeat(10_000) {
            list.addFirst(it)
        }

        assertEquals(10_000, list.size)
        repeat(10_000) {
            assertTrue(list.contains(it))
        }

        repeat(10_000) {
            list.removeLast()
        }
        assertEquals(0, list.size)
    }

    @Test
    fun `removeLast returns null when empty`() {
        val list = SnapshotList()

        val lastItem = list.removeLast()

        assertNull(lastItem)
    }
}
