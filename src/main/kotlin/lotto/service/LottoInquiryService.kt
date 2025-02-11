package lotto.service

import common.business.BusinessService
import common.business.Read
import common.business.Transaction
import lotto.domain.implementation.LottoReader
import lotto.service.dto.LottoBillData
import lotto.service.dto.LottoPublishData
import lotto.service.dto.PurchaseData
import purchase.domain.implementation.PurchaseReader

@BusinessService
class LottoInquiryService(
    private val lottoReader: LottoReader,
    private val purchaseReader: PurchaseReader,
) {
    @Transaction
    @Read
    fun getLottoes(memberId: String): List<LottoBillData> {
        val bills = lottoReader.findBill(memberId)
        return bills.map {
            LottoBillData(
                it.getId()!!,
                PurchaseData.from(
                    purchaseReader.findPurchase(purchaseId = it.getPurchaseId())
                ),
                LottoPublishData.from(
                    lottoReader.findLottoPublish(lottoPublishId = it.getLottoPublishId()),
                    lottoReader.findPublishedLotto(publishId = it.getLottoPublishId())
                )
            )
        }
    }
}
