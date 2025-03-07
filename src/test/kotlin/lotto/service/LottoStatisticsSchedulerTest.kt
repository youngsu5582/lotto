package lotto.service

import app.TestConfig
import config.ImplementationTest
import config.MockingClock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.LocalDateTime

@ImplementationTest
// TODO 해당 부분 역시, 배치로 분리되면 제거되어야 한다
@Import(TestConfig::class)
class LottoStatisticsSchedulerTest {
    @Autowired
    private lateinit var clock: MockingClock

    @MockitoBean
    private lateinit var taskScheduler: TaskScheduler

    @MockitoBean
    private lateinit var lottoStatisticsService: LottoStatisticsService

    @Autowired
    private lateinit var scheduler: LottoStatisticsScheduler

    @Test
    fun `5분마다 스케줄을 실행한다`() {
        val fixedTime = LocalDateTime.of(2024, 8, 21, 10, 0)
        clock.setInstant(fixedTime)
        scheduler.scheduleTask()

        val cronTriggerCaptor = ArgumentCaptor.captor<CronTrigger>()
        val runnableCaptor = ArgumentCaptor.captor<Runnable>()
        verify(taskScheduler).schedule(runnableCaptor.capture(), cronTriggerCaptor.capture())
        runnableCaptor.value.run()

        assertEquals("0 0/5 * * * ?", cronTriggerCaptor.value.expression)
        verify(lottoStatisticsService, Mockito.atLeastOnce()).updateStaticInfoWithCurrentLottoRoundInfo(fixedTime)
    }
}
