package me.masssh.kuake

import reactor.core.publisher.Flux

abstract class KuakeMapper {
    val kuakeClient: KuakeClient by lazy {
        val connectionFactory = KuakeConnectionFactoryRegistry.get() ?: throw IllegalStateException(
            "No connection factory found. You must register ConnectionFactory by KuakeConnectionFactoryRegistry."
        )
        KuakeClient(connectionFactory)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> select(query: () -> String) = kuakeClient.queryObject(query(), T::class) as Flux<T>

    inline fun insert(query: () -> String) = execute(query)

    inline fun update(query: () -> String) = execute(query)

    inline fun delete(query: () -> String) = execute(query)

    inline fun execute(query: () -> String) = kuakeClient.execute(query())
}
