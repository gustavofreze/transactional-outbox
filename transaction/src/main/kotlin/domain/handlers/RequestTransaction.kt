package domain.handlers

import domain.boundaries.Transactions
import domain.handlers.usecases.RequestTransaction
import domain.models.Transaction
import javax.enterprise.context.ApplicationScoped
import domain.handlers.commands.RequestTransaction as Command

@ApplicationScoped
class RequestTransaction(private val transactions: Transactions) : RequestTransaction {

    override fun handle(command: Command) {
        val event = Transaction.request(amount = command.amount, country = command.country)

        transactions.save(event = event)
    }
}