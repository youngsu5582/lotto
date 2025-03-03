import java.lang.management.ManagementFactory
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

object ConcurrentTestUtil {

    /**
     * [numberOfRequests] 개의 스레드에서 동시에 [block]을 실행합니다.
     * 각 작업의 결과는 [Result] 형태로 [ConcurrentLinkedQueue]에 담겨 반환됩니다.
     */
    fun concurrent(numberOfRequests: Int = 10, wait: Long = 5, block: () -> Unit): ConcurrentResult {
        val executor = Executors.newFixedThreadPool(numberOfRequests, object : ThreadFactory {
            private val counter = AtomicInteger(1)
            override fun newThread(r: Runnable): Thread {
                return Thread(r, "ConcurrentTestUtil-thread-${counter.getAndIncrement()}")
            }
        })
        val latch = CountDownLatch(1)  // 모든 작업을 동시에 시작시키기 위한 래치
        val results = ConcurrentLinkedQueue<Result<Unit>>() // 각 작업의 결과를 저장할 concurrent 컬렉션

        // [numberOfRequests] 만큼 Callable 작업 생성
        val tasks = (1..numberOfRequests).map {
            Callable {
                // latch가 해제될 때까지 대기
                latch.await()
                try {
                    block()
                    results.add(Result.success(Unit))
                } catch (e: Exception) {
                    results.add(Result.failure(e))
                }
            }
        }

        // 작업들을 executor에 제출
        tasks.forEach { executor.submit(it) }

        // 모든 스레드가 동시에 실행되도록 latch 해제
        latch.countDown()

        // 작업 완료 대기: wait(초) 동안 1초 간격으로 대기하며, 대기 도중 스레드 덤프 찍기
        executor.shutdown()
        var waitedSeconds = 0L
        while (!executor.awaitTermination(1, TimeUnit.SECONDS) && waitedSeconds < wait) {
            waitedSeconds++
            println("===== ${waitedSeconds}초 경과 후 Executor 스레드 덤프 =====")
        }
        if (!executor.isTerminated) {
            println("Executor가 ${wait}초 이내에 종료되지 않아 강제 종료합니다.")
            executor.shutdownNow()
        }
        return ConcurrentResult(results)
    }

    fun threadDumpForExecutor() {
        val threadMXBean = ManagementFactory.getThreadMXBean()
        val threadInfos = threadMXBean.dumpAllThreads(true, true)
        // 커스텀 스레드 이름 접두어를 사용하여 필터링
        val executorThreads = threadInfos.filter { it.threadName.startsWith("ConcurrentTestUtil-thread-") }
        println("===== EXECUTOR THREAD DUMP =====")
        executorThreads.forEach { info ->
            println("Thread: ${info.threadName} (ID: ${info.threadId}, State: ${info.threadState})")
            info.stackTrace.forEach { element ->
                println("\tat $element")
            }
            println()
        }
        println("===== END EXECUTOR THREAD DUMP =====")
    }
}

data class ConcurrentResult(
    val results: ConcurrentLinkedQueue<Result<Unit>>
) {
    fun getErrorMessage(): List<String> {
        return results.filter { it.isFailure }
            .mapNotNull { it.exceptionOrNull() }
            .mapNotNull { it.message }
    }

    fun successCount() = results.count { it.isSuccess }
    fun failureCount() = results.count { it.isFailure }
    fun <T : Throwable> failureCount(type: KClass<T>): Int {
        return results.filter { it.isFailure }
            .mapNotNull { it.exceptionOrNull() }
            .filterIsInstance(type.java)
            .count()
    }
}
