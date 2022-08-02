package driver.kafka.checkout.policies

import domain.handlers.commands.RequestTransaction
import driver.kafka.CommandBus
import javax.enterprise.context.ApplicationScoped
import domain.handlers.usecases.RequestTransaction as UseCase

@ApplicationScoped
class RequestTransaction(private val useCase: UseCase) : CommandBus<RequestTransaction> {

    override fun dispatch(command: RequestTransaction) {
        useCase.handle(command = command)
    }
}
