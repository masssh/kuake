package me.masssh.kuake

import io.r2dbc.spi.ConnectionFactories
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.kotlin.test.test
import java.time.LocalDate

class MapperTest {
    @Test
    fun `should select and get all columns by map`() {
        val connectionFactory = ConnectionFactories.get("r2dbc:mysql://testuser:testpass@127.0.0.1:3306/testdb")
        val mapper = Mapper(connectionFactory)
        val query = "SELECT 'foo' as a, 2 as b, CAST('2021-01-01' AS DATE) as c"
        mapper.execute(query)
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
}
