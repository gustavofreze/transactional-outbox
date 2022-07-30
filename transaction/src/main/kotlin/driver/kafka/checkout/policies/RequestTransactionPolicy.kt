package driver.kafka.checkout.policies

import domain.handlers.commands.RequestTransaction
import domain.models.Amount
import domain.models.Country
import domain.models.Positive
import driver.kafka.CommandBus
import driver.kafka.Policy
import driver.kafka.checkout.factories.events.CheckoutDone
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequestTransactionPolicy(private val commandBus: CommandBus<RequestTransaction>) : Policy<CheckoutDone> {

    override val subscribedEvent = CheckoutDone::class

    override fun handle(event: CheckoutDone) {
        val command = RequestTransaction(
            amount = Amount(
                positive = Positive(value = event.amount.value),
                currency = event.amount.currency
            ),
            country = Country(alpha2 = event.country.alpha2)
        )

        commandBus.dispatch(command = command)
    }
}
