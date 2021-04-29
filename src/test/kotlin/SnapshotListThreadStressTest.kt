import org.junit.Test
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SnapshotListThreadStressTest {

    private val processorCount = Runtime.getRuntime().availableProcessors()
    private val itemsPerProcessor = 1_000
    private val totalSize = processorCount * itemsPerProcessor

    @Test
    fun `add in parallel`() {
        val list = SnapshotList()

        // First processor adds 0-99, second processor adds 100-199, etc.
        runInParallel { processor ->
            repeat(itemsPerProcessor) { i ->
                list.addFirst((processor * itemsPerProcessor) + i)
            }
        }

        assertEquals(totalSize, list.size)
        repeat(totalSize) { i ->
            assertTrue(list.contains(i), "Expected list to contain $i")
        }
    }

    @Test
    fun `remove in parallel`() {
        val list = SnapshotList()
        repeat(totalSize) { i ->
            list.addFirst(i)
        }

        // Remove all the items, using each processor to remove a subset of the items.
        runInParallel {
            repeat(itemsPerProcessor) {
                // Removes will be interleaved, so we don't know which values any particular
                // processor will get, but since we're not removing more items than the list
                // contains we _do_ know the list will always contain at least one item, so this
                // should never return null.
                val removedItem = list.removeLast()
                assertNotNull(removedItem)
            }
        }

        assertEquals(0, list.size)
    }

    @Test
    fun `add and remove in parallel`() {
        val list = SnapshotList()

        // Each processor adds some items, then removes them.
        runInParallel { processor ->
            repeat(itemsPerProcessor) { i ->
                list.addFirst((processor * itemsPerProcessor) + i)
            }

            repeat(itemsPerProcessor) { i ->
                assertTrue(list.contains(i), "Expected list to contain $i")
            }

            repeat(itemsPerProcessor) {
                val removedItem = list.removeLast()
                assertNotNull(removedItem)
            }
        }

        assertEquals(0, list.size)
    }

    /**
     * Runs [block] on each processor. The block is passed the index of the processor it's using.
     */
    private fun runInParallel(block: (processor: Int) -> Unit) {
        // Use a latch so no blocks will execute until all threads are started.
        val allReady = CountDownLatch(1)

        // Launch as many threads as we have processors and store them in a list, so we can clean
        // them up later.
        val threads = List(processorCount) { processor ->
            thread(start = true, isDaemon = false) {
                allReady.await()
                block(processor)
            }
        }

        // Tell all threads to start executing their blocks.
        allReady.countDown()

        // Wait for all threads to finish.
        threads.forEach { it.join() }
    }
}
