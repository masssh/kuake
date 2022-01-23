package me.masssh.kuake

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import mu.KotlinLogging
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux

class Mapper(
    private val connectionFactory: ConnectionFactory
) {
    private val log = KotlinLogging.logger {}

    fun execute(query: String): Flux<Map<String, Any?>> {
        log.info { query }
        return executeQuery(query)
    }

    private fun executeQuery(query: String): Flux<Map<String, Any?>> =
        connectionFactory.use { connection ->
            connection.createStatement(query)
                .execute()
                .toFlux()
                .flatMap { result ->
                    result.map { row, rowMetaData -> createColumnMap(row, rowMetaData) }
                }
        }

    private fun <T> ConnectionFactory.use(
        action: (Connection) -> Flux<Map<String, T?>>
    ): Flux<Map<String, T?>> = Flux.usingWhen(this.create(), action, Connection::close)

    private fun createColumnMap(
        row: Row,
        rowMetaData: RowMetadata
    ): Map<String, Any?> = rowMetaData
        .columnNames.associateWith { columnName ->
            row.get(columnName).also {
                log.info { "columnName=$columnName value=$it" }
            }
        }
}
