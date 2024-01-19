package com.kspdata

import io.micronaut.data.annotation.Embeddable
import io.micronaut.data.annotation.EmbeddedId
import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository
import java.time.Instant
import java.util.*

@Embeddable
data class TheEmbeddedId(
    val a: UUID,
    val b: String,
    val c: String,
)

data class TheEntity(
    @EmbeddedId val id: TheEmbeddedId,
    val ownerIdType: String,
    val value: Double,
    val updated: Instant,
)

@JdbcRepository(dialect = Dialect.POSTGRES)
abstract class TheEntityRepository : GenericRepository<TheEntity, TheEmbeddedId> {

    @Query(
        """
            INSERT INTO the_entity (id_a, id_b, id_c, value, updated)
                 VALUES (:a, :b, :c, :value, :updated)
            ON CONFLICT ON CONSTRAINT pk_the_entity DO UPDATE
                    SET value = :value, updated = :updated;
        """,
        readOnly = false,
    )
    abstract suspend fun upsert(entities: TheEntity)
}
