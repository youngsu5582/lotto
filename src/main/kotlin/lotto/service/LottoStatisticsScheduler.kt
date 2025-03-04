package lotto.service

import common.business.BusinessService
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import java.time.Clock
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@BusinessService
class LottoStatisticsScheduler(
    private val clock: Clock,
    private val taskScheduler: TaskScheduler,
    private val lottoStatisticsService: LottoStatisticsService
) {
    private val cronExpression = "0 0/5 * * * ?"

    @PostConstruct
    fun scheduleTask() {
        val trigger = CronTrigger(cronExpression)
        taskScheduler.schedule({
            val time = LocalDateTime.now(clock)
            logger.info { "로또 통계 정보 갱신을 시작합니다. 시작 시간 : $time" }
            val result = lottoStatisticsService.updateStaticInfoWithCurrentLottoRoundInfo(time)
            logger.info { "로또 통계 정보 갱신 완료. 회차번호 : ${result.lottoRoundInfoId}" }
        }, trigger)
    }
}
