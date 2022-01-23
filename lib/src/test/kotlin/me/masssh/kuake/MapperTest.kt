package me.masssh.kuake

import org.junit.jupiter.api.Test
import reactor.kotlin.test.test

class MapperTest {
    @Test
    fun `should select and get all columns`() {
        val mapper = Mapper()
        val query = "SELECT 'foo' as a, 'bar' as b, 'baz' as c"
        mapper.execute(query).test()
            .expectNext(
                mapOf(
                    "a" to "foo",
                    "b" to "bar",
                    "c" to "baz",
                )
            ).verifyComplete()
    }
}
