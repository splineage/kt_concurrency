import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

var counter = 0

/**
 * 예상 결과 2100이 나오지 않음.
 * atomic operations 위반반 */
fun main() = runBlocking{
//    val workerA = asyncIncrement(2000)
//    val workerB = asyncIncrement(100)
//    workerA.await()
//    workerB.await()
//    println("counter $counter")
    val time = measureTimeMillis {
        /**
         * getName() 과 getLastName() 사이 의존성이 없기 때문에 개별실행.
         */
        val name = async { getName() }
        val lastName = async { getLasName() }
        println("${name.await()} ${lastName.await()}")
    }
    println("Execution took $time ms")
}

fun asyncIncrement(by: Int) = GlobalScope.async {
    for (i in 0 until by){
        counter++
    }
}

suspend fun getName(): String{
    delay(1000)
    return "JS"
}
suspend fun getLasName(): String{
    delay(1000)
    return "Kim"
}