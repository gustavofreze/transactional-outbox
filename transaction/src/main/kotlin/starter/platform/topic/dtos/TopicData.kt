package starter.platform.topic.dtos

data class TopicData(val name: String, val numPartitions: Int = 1, val replicationFactor: Int = 1)
