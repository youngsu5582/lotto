package lotto.controller

import auth.domain.vo.AuthenticatedMember
import common.dto.apiResponse
import common.web.Get
import common.web.HttpController
import lotto.service.LottoInquiryService
import lotto.service.dto.LottoBillData
import lotto.service.dto.LottoPublishData
import lotto.service.dto.LottoRoundInfoData
import java.time.LocalDateTime

@HttpController
class LottoInquiryController(
    private val lottoInquiryService: LottoInquiryService
) {
    @Get("/api/lottoes")
    fun lottoes(
        authenticatedMember: AuthenticatedMember
    ) = apiResponse<BillsResponse> {
        status = 200
        message = "본인 로또 전부 조회"
        data = BillsResponse(lottoInquiryService.getLottoes(authenticatedMember.memberId).map { BillResponse.from(it) })
    }
}

data class BillsResponse(
    val response: List<BillResponse>
)

data class BillResponse(
    val billId: Long,
    val paymentResponse: PurchaseResponse,
    val publishResponse: LottoPublishResponse
) {
    companion object {
        fun from(billData: LottoBillData): BillResponse {
            return BillResponse(
                billId = billData.id,
                paymentResponse = PurchaseResponse.from(billData.purchase),
                publishResponse = LottoPublishResponse.from(billData.lottoPublish)
            )
        }
    }
}

data class LottoPublishResponse(
    val status: String,
    val lottoRoundInfo: LottoRoundInfoData,
    val lottoes: List<List<Int>>,
    val issuedAt: LocalDateTime
) {
    companion object {
        fun from(purchaseData: LottoPublishData): LottoPublishResponse {
            return LottoPublishResponse(
                status = purchaseData.status,
                lottoRoundInfo = purchaseData.lottoRoundInfo,
                issuedAt = purchaseData.issuedAt,
                lottoes = purchaseData.lottoes
            )
        }
    }
}
