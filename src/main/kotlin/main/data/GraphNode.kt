package main.data

data class GraphNode(
    val id: String,
    val additionalData: Map<String, Any>
) {
    override fun equals(other: Any?): Boolean {
        return id == (other as? GraphNode)?.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}