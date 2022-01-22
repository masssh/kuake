package me.masssh.kuake

import mu.KotlinLogging

data class Record(val name: String)

class Mapper {
    val log = KotlinLogging.logger {}

    fun execute(query: String): Record {
        log.info { query }
        return Record("dummy")
    }
}
