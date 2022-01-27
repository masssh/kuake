package me.masssh.kuake

import io.r2dbc.spi.ConnectionFactories
import me.masssh.kuake.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.kotlin.test.test
import java.time.LocalDate

class KuakeClientTest {
    companion object {
        private const val CONNECTION_URL = "r2dbc:mysql://testuser:testpass@127.0.0.1:3306/testdb"
    }

    @Test
    fun `should select and get object`() {
        val connectionFactory = ConnectionFactories.get(CONNECTION_URL)
        val client = KuakeClient(connectionFactory)
        val query = "SELECT 'foo' as a, 2 as b, CAST('2021-01-01' AS DATE) as c"
        val expected = Model("foo", 2L, LocalDate.of(2021, 1, 1))
        client.queryObject(query, Model::class)
            .test()
            .assertNext { row ->
                assertThat(row).isEqualTo(expected)
            }.verifyComplete()
    }

    @Test
    fun `should throw given not enough columns by query`() {
        val connectionFactory = ConnectionFactories.get(CONNECTION_URL)
        val client = KuakeClient(connectionFactory)
        val query = "SELECT 'foo' as a, 2 as b"
        client.queryObject(query, Model::class)
            .test()
            .expectError(IllegalArgumentException::class.java)
            .verify()
    }
}
