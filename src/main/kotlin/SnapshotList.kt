class SnapshotList {

    private var head: Node? = null
    private var tail: Node? = null

    var size: Int = 0
        private set

    fun addFirst(item: Any) {
        val newHead = Node(data = item, next = head, previous = null)
        head?.previous = newHead
        head = newHead
        size++

        // Special case when the list is empty.
        if (tail === null) {
            tail = head
        }
    }

    fun removeLast(): Any? {
        val removedNode = tail ?: return null
        tail = removedNode.previous
        tail?.next = null
        size--

        // Special case when the last item is being removed.
        if (head === removedNode) {
            head = null
        }

        return removedNode.data
    }

    fun contains(item: Any): Boolean {
        forEach {
            if (it == item) return true
        }
        return false
    }

    private inline fun forEach(block: (Any) -> Unit) {
        var currentNode = head
        while (currentNode != null) {
            block(currentNode.data)
            currentNode = currentNode.next
        }
    }

    private class Node(
        val data: Any,
        var next: Node?,
        var previous: Node?
    )
}
