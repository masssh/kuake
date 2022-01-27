package me.masssh.kuake

import me.masssh.kuake.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import java.time.LocalDate

class TestDao {
    fun createTable(): Mono<Int> = kexecute {
        """
            CREATE TABLE model (
                a CHAR(32) NOT NULL
                ,b INT NOT NULL
                ,c DATE NOT NULL
            )
        """.trimIndent()
    }

    fun insert(): Mono<Int> = kexecute { "INSERT INTO model (a, b, c) VALUES ('foo', 2, '2021-01-01')" }
    fun select(): Flux<Model> = kquery { "SELECT a, b, c FROM model" }
    fun update(): Mono<Int> = kexecute { "UPDATE model SET a='bar', b=3, c='2021-01-02'" }
    fun delete(): Mono<Int> = kexecute { "DELETE FROM model" }
    fun dropTable(): Mono<Int> = kexecute { "DROP TABLE model" }
}

class KuakeExtensionsTest {

    @Test
    fun `should basic queries work`() {
        val testDao = TestDao()
        val inserted = Model("foo", 2L, LocalDate.of(2021, 1, 1))
        val updated = Model("bar", 3L, LocalDate.of(2021, 1, 2))
        testDao.createTable().test().assertNext { assertThat(it).isEqualTo(0) }.verifyComplete()
        testDao.insert().test().assertNext { assertThat(it).isEqualTo(1) }.verifyComplete()
        testDao.select().test().assertNext { assertThat(it).isEqualTo(inserted) }.verifyComplete()
        testDao.update().test().assertNext { assertThat(it).isEqualTo(1) }.verifyComplete()
        testDao.select().test().assertNext { assertThat(it).isEqualTo(updated) }.verifyComplete()
        testDao.delete().test().assertNext { assertThat(it).isEqualTo(1) }.verifyComplete()
        testDao.select().test().expectNextCount(0).verifyComplete()
        testDao.dropTable().test().assertNext { assertThat(it).isEqualTo(0) }.verifyComplete()
        testDao.select().test().expectNextCount(0).verifyError()
    }
}
