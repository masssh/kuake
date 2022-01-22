package me.masssh.kuake

import org.junit.jupiter.api.Test

class MapperTest {
    @Test
    fun test() {
        val mapper = Mapper()
        val query = "SELECT * FROM table"
        mapper.execute(query)
    }
}
