package lotto.service

import common.business.BusinessService
import common.business.Read
import common.business.Transaction
import lotto.domain.implementation.LottoReader
import lotto.service.dto.LottoBillData
import lotto.service.dto.LottoPublishData
import lotto.service.dto.PurchaseData

@BusinessService
class LottoInquiryService(
    private val lottoReader: LottoReader,
) {
    @Transaction
    @Read
    fun getLottoes(memberId: String): List<LottoBillData> {
        val bills = lottoReader.findBill(memberId)
        return bills.map {
            LottoBillData(
                it.getId()!!,
                PurchaseData.from(
                    it.getPurchase()
                ),
                LottoPublishData.from(
                    it.getLottoPublish(),
                    lottoReader.findPublishedLotto(it.getLottoPublish().getId())
                )
            )
        }
    }
}
