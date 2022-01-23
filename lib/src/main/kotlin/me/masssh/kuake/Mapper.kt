package me.masssh.kuake

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactories
import mu.KotlinLogging
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux

class Mapper {
    private val log = KotlinLogging.logger {}
    private val connectionFactory = ConnectionFactories.get("r2dbc:mysql://testuser:testpass@127.0.0.1:3306/testdb")

    fun execute(query: String): Flux<String> {
        log.info { query }
        return Flux.usingWhen(
            connectionFactory.create(),
            { connection: Connection ->
                connection.createStatement(query)
                    .execute()
                    .toFlux()
                    .flatMap { result ->
                        result.map { row, _ ->
                            val column = row.get(0, String::class.java)
                            log.info { "index=0 value=$column" }
                            column
                        }
                    }
            },
            Connection::close
        )
    }
}
