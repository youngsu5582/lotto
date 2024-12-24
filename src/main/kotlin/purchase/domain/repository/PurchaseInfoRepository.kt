package purchase.domain.repository

import org.springframework.data.repository.CrudRepository
import purchase.domain.entity.PurchaseInfo
import java.util.*

interface PurchaseInfoRepository : CrudRepository<PurchaseInfo, UUID> {
}
