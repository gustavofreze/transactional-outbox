package driver.kafka.mapper

import driver.kafka.Event
import kotlin.reflect.KClass

interface EventMapper {

    fun toJson(event: Any): String

    fun <T : Event> toEvent(json: String, type: KClass<T>): T
}
