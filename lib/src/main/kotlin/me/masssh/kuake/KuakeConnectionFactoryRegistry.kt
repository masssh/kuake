package me.masssh.kuake

import io.r2dbc.spi.ConnectionFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

object KuakeConnectionFactoryRegistry {
    private var defaultConnectionFactory: AtomicReference<ConnectionFactory?> = AtomicReference(null)
    private val connectionFactories: ConcurrentHashMap<String, ConnectionFactory> = ConcurrentHashMap()

    fun register(name: String, connectionFactory: ConnectionFactory) {
        connectionFactories[name] = connectionFactory
        defaultConnectionFactory.compareAndSet(null, connectionFactory)
    }

    fun registerDefault(name: String, connectionFactory: ConnectionFactory) {
        connectionFactories[name] = connectionFactory
        defaultConnectionFactory.set(connectionFactory)
    }

    fun get(): ConnectionFactory? = defaultConnectionFactory.get()

    fun get(name: String): ConnectionFactory? = connectionFactories[name]
}
