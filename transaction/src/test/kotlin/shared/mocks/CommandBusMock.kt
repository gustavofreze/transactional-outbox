package shared.mocks

import domain.handlers.commands.RequestTransaction
import driver.kafka.CommandBus
import io.quarkus.test.Mock
import javax.inject.Singleton

@Mock
@Singleton
class CommandBusMock : CommandBus<RequestTransaction> {

    val commands = mutableListOf<RequestTransaction>()

    override fun dispatch(command: RequestTransaction) {
        commands.add(command)
    }
}
