package lotto.service

import common.business.BusinessService
import lotto.domain.entity.LottoPublish
import lotto.domain.implementation.LottoPaperGenerator
import lotto.domain.implementation.LottoPublisher
import lotto.domain.vo.LottoNumbers
import lotto.service.dto.LottoOrderData
import order.domain.implementation.OrderProcessor
import order.domain.vo.OrderData
import order.domain.vo.OrderRequest
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime

@BusinessService
class LottoOrderService(
    private val lottoPaperGenerator: LottoPaperGenerator,
    private val lottoPublisher: LottoPublisher,
    private val orderProcessor: OrderProcessor,
    private val clock: Clock,
) {
    fun saveLottoOrder(
        lottoNumbers: LottoNumbers
    ): LottoOrderData {
        val (lottoPublish, amount) = publish(lottoNumbers)
        val order = orderProcessor.saveOrder(OrderRequest(amount))
        return LottoOrderData(
            lottoPublishId = lottoPublish.getId(),
            orderData = OrderData.from(order)
        )
    }

    private fun publish(lottoNumbers: LottoNumbers): Pair<LottoPublish, BigDecimal> {
        val issuedAt = LocalDateTime.now(clock)
        val lottoPaper = lottoPaperGenerator.generateWithNumbers(lottoNumbers)
        return Pair(lottoPublisher.publish(issuedAt, lottoPaper), lottoPaper.getAmount())
    }
}
