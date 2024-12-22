package config

import org.springframework.data.repository.CrudRepository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

abstract class FakeCrudRepository<T : Any, ID : Any> : CrudRepository<T, ID> {
    private val store: MutableMap<ID, T> = ConcurrentHashMap()

    override fun <S : T> save(entity: S): S {
        val id = extractId(entity) ?: throw IllegalArgumentException("Entity must have an ID")
        store[id] = entity
        return entity
    }

    override fun <S : T> saveAll(entities: Iterable<S>): Iterable<S> {
        entities.forEach { save(it) }
        return entities
    }

    override fun findById(id: ID): Optional<T> {
        return Optional.ofNullable(store[id])
    }

    override fun existsById(id: ID): Boolean {
        return store.containsKey(id)
    }

    override fun findAll(): Iterable<T> {
        return store.values
    }

    override fun findAllById(ids: Iterable<ID>): Iterable<T> {
        return ids.mapNotNull { store[it] }
    }

    override fun count(): Long {
        return store.size.toLong()
    }

    override fun deleteById(id: ID) {
        store.remove(id)
    }

    override fun delete(entity: T) {
        val id = extractId(entity) ?: throw IllegalArgumentException("Entity must have an ID")
        store.remove(id)
    }

    override fun deleteAll(entities: Iterable<T>) {
        entities.forEach { delete(it) }
    }

    override fun deleteAllById(ids: MutableIterable<ID>) {
        ids.forEach { deleteById(it) }
    }

    override fun deleteAll() {
        store.clear()
    }

    /**
     * Override this method to extract ID from the entity.
     */
    protected abstract fun extractId(entity: T): ID?
}
