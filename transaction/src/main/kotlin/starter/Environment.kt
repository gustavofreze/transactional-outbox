package starter

import java.lang.System.getenv

object Environment {

    fun get(variable: String): String {
        val template = "starter.Environment variable <%s> is missing."
        return getenv(variable) ?: error(template.format(variable))
    }
}
