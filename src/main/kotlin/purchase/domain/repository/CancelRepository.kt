package purchase.domain.repository

import org.springframework.data.repository.CrudRepository
import purchase.domain.entity.Cancel
import java.util.*

interface CancelRepository : CrudRepository<Cancel, UUID> {
}
