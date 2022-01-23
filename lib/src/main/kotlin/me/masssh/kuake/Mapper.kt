package me.masssh.kuake

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import mu.KotlinLogging
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import kotlin.reflect.KClass

class Mapper {
    private val log = KotlinLogging.logger {}
    private val connectionFactory = ConnectionFactories.get("r2dbc:mysql://testuser:testpass@127.0.0.1:3306/testdb")

    inline fun <reified T : Any> kClass(): KClass<T> = T::class

    fun execute(query: String): Flux<Map<String, String?>> {
        log.info { query }
        return executeQuery(query)
    }

    private fun executeQuery(query: String): Flux<Map<String, String?>> =
        connectionFactory.use { connection ->
            connection.createStatement(query)
                .execute()
                .toFlux()
                .flatMap { result ->
                    result.map { row, rowMetaData -> createColumnMap(row, rowMetaData) }
                }
        }

    private fun ConnectionFactory.use(
        action: (Connection) -> Flux<Map<String, String?>>
    ): Flux<Map<String, String?>> = Flux.usingWhen(this.create(), action, Connection::close)

    private fun createColumnMap(row: Row, rowMetaData: RowMetadata): Map<String, String?> = rowMetaData
        .columnNames.associateWith { columnName ->
            row.get(columnName, String::class.java).also {
                log.info { "columnName=$columnName value=$it" }
            }
        }
}
