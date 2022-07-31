package driver.kafka.mapper

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import driver.kafka.Event
import javax.enterprise.context.ApplicationScoped
import kotlin.reflect.KClass

@ApplicationScoped
class Adapter : EventMapper {

    private val mapper: ObjectMapper = jacksonObjectMapper()

    init {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.findAndRegisterModules()
    }

    override fun toJson(event: Any): String = mapper.writeValueAsString(event)

    override fun <T : Event> toEvent(json: String, type: KClass<T>): T = mapper.readValue(json, type.javaObjectType)
}
