package me.masssh.kuake

import io.r2dbc.spi.ConnectionFactories
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

const val CONNECTION_URL = "r2dbc:mysql://testuser:testpass@127.0.0.1:3306/testdb"
val mapper = Mapper(ConnectionFactories.get(CONNECTION_URL))

inline fun <reified T> kquery(generator: () -> String): Flux<T> {
    val query = generator()
    val kClass = T::class
    @Suppress("UNCHECKED_CAST")
    return mapper.queryObject(query, kClass) as Flux<T>
}

inline fun kexecute(generator: () -> String): Mono<Int> {
    val query = generator()
    @Suppress("UNCHECKED_CAST")
    return mapper.execute(query)
}
