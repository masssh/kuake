package me.masssh.kuake

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.kotlin.test.test
import java.time.LocalDate

class MapperTest {
    @Test
    fun `should select and get all columns by map`() {
        val mapper = Mapper()
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
