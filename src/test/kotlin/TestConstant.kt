import lotto.domain.entity.LottoRoundInfo
import java.time.LocalDateTime

object TestConstant {
    // 의도적으로 시간을 매우 늘려서 사용자의 토큰을 받아놓았다.(expire : 16000000000000000)
    const val TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZmMzYWQ4Mi1lM2JhLTQ1MjAtYTk2OS0wYjRhMjkzYzlhOGIiLCJpYXQiOjE3Mzg4NTIyMzIsImV4cCI6MTYwMDE3Mzg4NTIyMzJ9.bzacSBSyi3gakhKau6-m2fZTY-VllRzq0m5Crf0-ZGs"

    // 의도적으로 시간을 매우 짧게 사용자의 토큰을 받아놓았다.(expire: 10)
    const val EXPIRED_TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZmMzYWQ4Mi1lM2JhLTQ1MjAtYTk2OS0wYjRhMjkzYzlhOGIiLCJpYXQiOjE3Mzg4NTMxMDksImV4cCI6MTczODg1MzEwOX0.I3ej_tEDUKtDpp7C2DowJGI6EI_F3RIRPhmwUhRYMmU"

    val DATE_TIME: LocalDateTime = LocalDateTime.now()
}
