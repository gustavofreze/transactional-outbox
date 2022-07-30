package domain.handlers.usecases

import domain.handlers.commands.RequestTransaction

interface RequestTransaction {

    fun handle(command: RequestTransaction)
}
