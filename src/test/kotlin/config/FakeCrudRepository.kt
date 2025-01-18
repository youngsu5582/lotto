package config

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.ConcurrentHashMap

abstract class FakeCrudRepository<T : Any, ID : Any> : CrudRepository<T, ID> {
    private val store: MutableMap<ID, T> = ConcurrentHashMap()


    @Transactional
    override fun <S : T> save(entity: S): S {
        val id = extractId(entity) ?: throw IllegalArgumentException("Entity must have an ID")
        store[id] = entity
        return entity
    }

    @Transactional
    override fun <S : T> saveAll(entities: Iterable<S>): Iterable<S> {
        entities.forEach { save(it) } // Save each entity with transactional guarantees
        return entities
    }

    @Transactional(readOnly = true)
    override fun findById(id: ID): Optional<T> {
        return Optional.ofNullable(store[id])
    }

    @Transactional(readOnly = true)
    override fun existsById(id: ID): Boolean {
        return store.containsKey(id)
    }

    @Transactional(readOnly = true)
    override fun findAll(): Iterable<T> {
        return store.values
    }

    @Transactional(readOnly = true)
    override fun findAllById(ids: Iterable<ID>): Iterable<T> {
        return ids.mapNotNull { store[it] }
    }

    @Transactional(readOnly = true)
    override fun count(): Long {
        return store.size.toLong()
    }

    @Transactional
    override fun deleteById(id: ID) {
        store.remove(id)
    }

    @Transactional
    override fun delete(entity: T) {
        val id = extractId(entity) ?: throw IllegalArgumentException("Entity must have an ID")
        store.remove(id)
    }

    @Transactional
    override fun deleteAll(entities: Iterable<T>) {
        entities.forEach { delete(it) }
    }

    @Transactional
    override fun deleteAllById(ids: MutableIterable<ID>) {
        ids.forEach { deleteById(it) }
    }

    @Transactional
    override fun deleteAll() {
        store.clear()
    }

    /**
     * Override this method to extract ID from the entity.
     */
    protected abstract fun extractId(entity: T): ID?
}
