package me.masssh.kuake

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.Result
import io.r2dbc.spi.Row
import mu.KotlinLogging
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class KuakeClient(
    private val connectionFactory: ConnectionFactory
) {
    private val log = KotlinLogging.logger {}

    fun queryObject(query: String, kClass: KClass<*>) = kClass.primaryConstructor?.let { primaryConstructor ->
        val params = primaryConstructor.parameters
        queryInternal(query, params).flatMap { columnMap ->
            try {
                @Suppress("UNCHECKED_CAST")
                val instance = primaryConstructor.callBy(columnMap)
                Flux.just(instance)
            } catch (e: Error) {
                Flux.error<IllegalStateException> {
                    IllegalStateException(
                        "The query result=$columnMap has not enough columns for primary constructor of $kClass."
                    )
                }
            }
        }
    } ?: Flux.error<IllegalStateException> {
        IllegalStateException("$kClass has no primary constructor.")
    }

    private fun queryInternal(query: String, resultParams: List<KParameter>) = executeInternal(query) { result ->
        result.map { row, _ -> createColumnMap(row, resultParams) }
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
        resultParams: List<KParameter>,
    ) = resultParams.associateWith { param ->
        param.name?.let { row[it] } ?: throw IllegalStateException("The field $param has no name.")
    }
}
