package purchase.config

import common.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import purchase.domain.PaymentClient
import purchase.domain.vo.PurchaseProvider
import toss.TossPaymentClient
import toss.config.TossClientConfig

@Config
@Import(TossClientConfig::class)
class PaymentClientConfig(
    private val tossPaymentClient: TossPaymentClient,
) {
    @Bean
    fun paymentClients(): Map<String, PaymentClient> {
        val paymentClients = HashMap<String, PaymentClient>()
        paymentClients[PurchaseProvider.TOSS.name] = tossPaymentClient
        return paymentClients
    }
}
