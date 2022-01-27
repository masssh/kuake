package me.masssh.kuake

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.Result
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import mu.KotlinLogging
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class Mapper(
    private val connectionFactory: ConnectionFactory
) {
    private val log = KotlinLogging.logger {}

    fun queryObject(query: String, kClass: KClass<*>) = queryInternal(query).flatMap { resultMap ->
        kClass.primaryConstructor?.let { primaryConstructor ->
            val paramMap = primaryConstructor.parameters.associateWith { kParam ->
                resultMap.getOrDefault(kParam.name!!, null)
            }
            try {
                @Suppress("UNCHECKED_CAST")
                val instance = primaryConstructor.callBy(paramMap)
                Flux.just(instance)
            } catch (e: Error) {
                Flux.error<IllegalArgumentException> {
                    IllegalArgumentException("query result has not enough columns for primary constructor of $kClass.")
                }
            }
        } ?: Flux.error<IllegalArgumentException> {
            IllegalArgumentException("$kClass has no primary constructor.")
        }
    }

    fun queryMap(query: String) = queryInternal(query)

    private fun queryInternal(query: String) = executeInternal(query) { result ->
        result.map { row, rowMetaData -> createColumnMap(row, rowMetaData) }
    }

    fun execute(query: String) = executeInternal(query) { result ->
        result.rowsUpdated
    }.toMono()

    private fun <T> executeInternal(query: String, processor: (Result) -> Publisher<T>): Flux<T> =
        connectionFactory.use { connection ->
            log.info { query }
            connection.createStatement(query)
                .execute()
                .toFlux()
                .flatMap(processor)
        }

    private fun <T> ConnectionFactory.use(
        action: (Connection) -> Flux<T>
    ) = Flux.usingWhen(this.create(), action, Connection::close)

    private fun createColumnMap(
        row: Row,
        rowMetaData: RowMetadata
    ) = rowMetaData.columnNames.associateWith { columnName ->
        row.get(columnName).also {
            log.info { "columnName=$columnName value=$it" }
        }
    }
}
