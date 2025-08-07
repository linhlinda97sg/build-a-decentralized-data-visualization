data class DecentralizedData(
    val id: String,
    val data: Map<String, Double>,
    val visualizationType: VisualizationType,
    val notificationThreshold: Double,
    val notificationType: NotificationType
)

enum class VisualizationType {
    LINE_GRAPH,
    BAR_CHART,
    PIE_CHART
}

enum class NotificationType {
    EMAIL,
    SMS,
    IN_APP_NOTIFICATION
}

data class Notification(
    val type: NotificationType,
    val message: String
)

data class DataPoint(
    val x: Double,
    val y: Double
)

interface DecentralizedNotifier {
    fun sendNotification(notification: Notification)
}

interface DataVisualization {
    fun visualize(data: List<DataPoint>): String
}

class DecentralizedDataNotifier(
    private val notificationService: DecentralizedNotifier,
    private val visualizationService: DataVisualization
) {
    fun process(data: DecentralizedData) {
        val dataPoints = data.data.map { DataPoint(it.key.toDouble(), it.value) }
        val visualization = visualizationService.visualize(dataPoints)
        if (dataPoints.any { it.y > data.notificationThreshold }) {
            val notification = Notification(data.notificationType, "Data threshold exceeded!")
            notificationService.sendNotification(notification)
        }
    }
}