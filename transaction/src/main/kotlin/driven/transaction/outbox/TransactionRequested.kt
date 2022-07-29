package driven.transaction.outbox

import com.lectra.koson.obj
import domain.events.TransactionRequested

class TransactionRequested(event: TransactionRequested) : TransactionEvent(event = event) {

    override val revision: Int = 1
    override val eventType: String = this::class.java.simpleName
    override val payload: String = obj {
        "id" to event.transactionId.value.toString()
        "amount" to obj {
            "value" to event.amount.positive.value.toString()
            "currency" to event.amount.currency.toString()
        }
        "country" to obj {
            "iso2" to event.country.iso2
        }
        "occurredOn" to event.occurredOn.toString()
    }.toString()
}