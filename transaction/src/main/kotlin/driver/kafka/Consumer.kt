package driver.kafka

import driver.kafka.outbox.Outbox

interface Consumer<T : Event> {

    val topic: String

    fun accept(record: Outbox)
}
