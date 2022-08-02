package shared.mocks

import io.quarkus.test.Mock
import javax.enterprise.inject.Instance
import javax.enterprise.util.TypeLiteral
import javax.inject.Singleton

@Mock
@Singleton
class InstanceMock<T>(private val list: MutableList<T>) : Instance<T>, MutableIterable<T> by list {

    override fun get(): T = TODO("Not yet implemented")

    override fun select(vararg p0: Annotation): Instance<T> = TODO("Not yet implemented")

    override fun <U : T> select(p0: Class<U>, vararg p1: Annotation): Instance<U> = TODO("Not yet implemented")

    override fun <U : T> select(p0: TypeLiteral<U>, vararg p1: Annotation): Instance<U> = TODO("Not yet implemented")

    override fun isUnsatisfied(): Boolean = TODO("Not yet implemented")

    override fun isAmbiguous(): Boolean = TODO("Not yet implemented")

    override fun destroy(p0: T) = TODO("Not yet implemented")
}
