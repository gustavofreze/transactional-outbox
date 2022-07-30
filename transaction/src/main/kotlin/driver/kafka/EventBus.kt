package driver.kafka

interface EventBus<T : Event> {

    fun dispatch(event: T)
}
