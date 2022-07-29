package domain.models

import java.util.UUID
import java.util.UUID.randomUUID

data class TransactionId(val value: UUID = randomUUID())