package driver.kafka

import domain.handlers.commands.Command

interface CommandBus<T : Command> {

    fun dispatch(command: T)
}
