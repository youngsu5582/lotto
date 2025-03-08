package lotto.service

import common.business.BusinessService
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import lotto.domain.repository.LottoRoundInfoRepository
import org.springframework.core.env.Environment
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import java.time.Clock
import java.time.LocalDateTime

private val log = KotlinLogging.logger {}

@BusinessService
class LottoStatisticsScheduler(
    private val clock: Clock,
    private val taskScheduler: TaskScheduler,
    private val lottoRoundInfoRepository: LottoRoundInfoRepository,
    private val lottoStatisticsService: LottoStatisticsService,
    private val environment: Environment
) {
    private val cronExpression = "0 0/5 * * * ?"

    // TODO 해당 부분은 배치 서버가 의도적으로 분리되어 있지 않아서 허나의 서버에서만 처리하게 지정
    // 차후, 배치 서버만 전담하게 분리해야 한다.
    @PostConstruct
    fun scheduleTask() {
        val batchEnabled =
            System.getenv("BATCH_ENABLED")?.toBoolean() ?: environment.getProperty("BATCH_ENABLED")?.toBoolean()
            ?: false
        if (!batchEnabled) {
            log.info { "이 서버에서는 배치 스케줄러를 실행하지 않습니다. (BATCH_ENABLED=false)" }
            return
        }

        taskScheduler.schedule({
            runCatching {
                val time = LocalDateTime.now(clock)
                val lottoRoundInfo = getLottoRoundInfo(time)
                log.info { "로또 통계 정보 갱신을 시작합니다. 회차번호 : ${lottoRoundInfo.round} 시작 시간 : $time" }
                lottoStatisticsService.updateStaticInfoWithCurrentLottoRoundInfo(lottoRoundInfo, time)
            }.onSuccess {
                log.info { "로또 통계 정보 갱신 완료. 회차번호 : ${it.round} 종료 시간 : ${LocalDateTime.now(clock)}" }
            }.onFailure {
                log.error(it) { "로또 통계 정보 갱신 중 오류가 발생했습니다." }
            }
        }, CronTrigger(cronExpression))
    }

    private fun getLottoRoundInfo(time: LocalDateTime) =
        lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(time)
            ?: throw IllegalArgumentException("$time 에 맞는 회치가 없습니다.")
}
