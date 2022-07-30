package domain.handlers.commands

import domain.models.Amount
import domain.models.Country

class RequestTransaction(val amount: Amount, val country: Country) : Command
