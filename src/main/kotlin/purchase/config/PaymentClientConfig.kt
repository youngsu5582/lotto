package purchase.config

import common.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import purchase.domain.PaymentClient
import purchase.domain.vo.PurchaseProvider
import toss.TossPaymentClient
import toss.config.TossClientConfig
import java.util.*

@Config
@Import(TossClientConfig::class)
class PaymentClientConfig(
    private val tossPaymentClient: TossPaymentClient,
) {
    @Bean
    fun paymentClients(): EnumMap<PurchaseProvider, PaymentClient> {
        return EnumMap<PurchaseProvider, PaymentClient>(PurchaseProvider::class.java).apply {
            put(PurchaseProvider.TOSS, tossPaymentClient)
        }
    }
}
