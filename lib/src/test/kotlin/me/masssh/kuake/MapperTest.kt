package me.masssh.kuake

import org.junit.jupiter.api.Test
import reactor.kotlin.test.test

class MapperTest {
    @Test
    fun `should select and get first column`() {
        val mapper = Mapper()
        val query = "SELECT 'foo'"
        mapper.execute(query).test().expectNext("foo").verifyComplete()
    }
}
