package lotto

import lotto.domain.entity.LottoRoundInfo
import lotto.domain.entity.LottoStatistics
import lotto.domain.entity.LottoStatus
import lotto.domain.repository.LottoRoundInfoRepository
import lotto.domain.repository.LottoStatisticsRepository
import member.domain.vo.MemberIdentifier
import member.service.MemberService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

@Component
@Profile("local", "dev")
class LottoInitializer : CommandLineRunner {
    @Autowired
    private lateinit var lottoStatisticsRepository: LottoStatisticsRepository
    private val log = LoggerFactory.getLogger(LottoInitializer::class.java)

    @Autowired
    private lateinit var lottoRoundInfoRepository: LottoRoundInfoRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var memberService: MemberService

    override fun run(vararg args: String?) {
        log.info("Server Setting Start")
        val time = LocalDateTime.now()
        if (lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(LocalDateTime.now()) != null) {
            log.info("Already Setting")
            return
        }

        val roundInfo = lottoRoundInfoRepository.save(
            LottoRoundInfo(
                status = LottoStatus.ONGOING,
                startDate = time.minusDays(1),
                endDate = time.plusYears(1),
                drawDate = time.plusYears(2),
                round = 1000L,
                paymentDeadline = time.plusYears(1).plusHours(1)
            )
        )
        lottoStatisticsRepository.save(
            LottoStatistics(
                lottoRoundInfoId = roundInfo.id!!,
                memberCount = 0,
                lottoPublishCount = 0,
                totalPurchaseMoney = BigDecimal.ZERO,
                updatedAt = LocalDateTime.now()
            )
        )

        memberService.registerMember(MemberIdentifier("test@example.com", "password"))
        val sqlFilePath = "script/local_lotto.sql"
        val sqlContent = Files.readString(Paths.get(sqlFilePath))
        try {
            jdbcTemplate.execute(sqlContent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        log.info("${sqlContent.length} Lotto Data Inserted")
        log.info("Server Setting Finished")
    }
}
