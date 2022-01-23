package me.masssh.kuake

import io.r2dbc.spi.ConnectionFactories
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.kotlin.test.test
import java.time.LocalDate

class MapperTest {
    companion object {
        private const val CONNECTION_URL = "r2dbc:mysql://testuser:testpass@127.0.0.1:3306/testdb"
    }

    @Test
    fun `should select and get all columns by map`() {
        val connectionFactory = ConnectionFactories.get(CONNECTION_URL)
        val mapper = Mapper(connectionFactory)
        val query = "SELECT 'foo' as a, 2 as b, CAST('2021-01-01' AS DATE) as c"
        mapper.selectMap(query)
            .test()
            .assertNext { row ->
                assertThat(row).isEqualTo(
                    mapOf(
                        "a" to "foo",
                        "b" to 2L,
                        "c" to LocalDate.of(2021, 1, 1),
                    )
                )
            }.verifyComplete()
    }

    @Test
    fun `should select and get object`() {
        data class Model(val a: String, val b: Long, val c: LocalDate)

        val connectionFactory = ConnectionFactories.get(CONNECTION_URL)
        val mapper = Mapper(connectionFactory)
        val query = "SELECT 'foo' as a, 2 as b, CAST('2021-01-01' AS DATE) as c"
        val expected = Model("foo", 2L, LocalDate.of(2021, 1, 1))
        mapper.selectObject(query, Model::class)
            .test()
            .assertNext { row ->
                assertThat(row).isEqualTo(expected)
            }.verifyComplete()
    }
}
