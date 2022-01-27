package me.masssh.kuake

import io.r2dbc.spi.ConnectionFactories
import me.masssh.kuake.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import java.time.LocalDate


class KuakeMapperTest {
    companion object {
        private const val CONNECTION_URL = "r2dbc:mysql://testuser:testpass@127.0.0.1:3306/testdb"
    }

    class TestDao : KuakeMapper() {
        fun createTable(): Mono<Int> = execute {
            """
            CREATE TABLE model (
                a CHAR(32) NOT NULL
                ,b INT NOT NULL
                ,c DATE NOT NULL
            )
        """.trimIndent()
        }

        fun dropTable(): Mono<Int> = execute { "DROP TABLE model" }

        fun select(): Flux<Model> = select { "SELECT a, b, c FROM model" }

        fun insert(): Mono<Int> = insert { "INSERT INTO model (a, b, c) VALUES ('foo', 2, '2021-01-01')" }

        fun update(): Mono<Int> = update { "UPDATE model SET a='bar', b=3, c='2021-01-02'" }

        fun delete(): Mono<Int> = delete { "DELETE FROM model" }
    }

    @Test
    fun `basic queries should work`() {
        KuakeConnectionFactoryRegistry.register("test", ConnectionFactories.get(CONNECTION_URL))
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
