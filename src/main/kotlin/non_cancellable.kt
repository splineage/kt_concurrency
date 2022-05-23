import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun nonCancellable() = runBlocking{
    val duration = measureTimeMillis {
        val job = launch {
            try {
                while(isActive){
                    delay(500)
                    println("still rinning")
                }
            }finally {
                // 취소 중인 코루틴은 일시 중단될 수 없음.
                // 일시 중지가 필요한 경우 NonCancellable 를 사용해야 함.
                withContext(NonCancellable){
                    println("cancelled, will end now")
                    delay(5000)
                    println("delay completed")
                }
            }
        }
        delay(1200)
        job.cancelAndJoin()
//        job.cancel() -> main 이 먼저 종료.
    }
    println("Took $duration ms")
}