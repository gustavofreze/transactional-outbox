package driver.kafka

import kotlin.reflect.KClass

interface Policy<T : Event> {

    val subscribedEvent: KClass<T>

    fun handle(event: T)
}
