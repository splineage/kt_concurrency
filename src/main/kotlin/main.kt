import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.Action

data class UserInfo(val name: String, val lastName: String, val id: Int)
lateinit var user: UserInfo

fun main(): Unit = runBlocking{
//    asyncGetUserInfo(1)
//    delay(1000)
//    println("${Thread.activeCount()} threads active at the start")
//    val time = measureTimeMillis {
//        createCoroutines(3)
//    }
//    println("${Thread.activeCount()} threads active at the end")
//    println("Took $time ms")
//    println("User ${user.id} is ${user.name}")

//    context_example_async()

//    iterator_example()

    /**
     * channel
     */
    var time = measureTimeMillis {
        val channel = Channel<Int>()
        val sender = GlobalScope.launch {
            repeat(10){
                channel.send(it)
                println("Send $it")
            }
        }
        channel.receive()
        channel.receive()

    }
    println("channel Took $time")

    time = measureTimeMillis {
        val unBufferedChannel = Channel<Int>(Channel.UNLIMITED) // 송신자 중지 안함.
        val sender = GlobalScope.launch {
            repeat(5){
                println("Sending $it")
                unBufferedChannel.send(it)
            }
        }
        delay(500)
    }
    println("unbufferedchannel took $time")

    time = measureTimeMillis {
        val channel = Channel<Int>(4) // 버퍼 크기에 의해 중단됨.
        val sender = GlobalScope.launch {
            repeat(10){
                channel.send(it)
                println("Send $it")
            }
        }
        delay(500)
        println("Taking two")
        channel.receive()
        delay(500)
    }
    println("unbufferedchannel took $time")

    time = measureTimeMillis {
        val workerA = asyncActorIncrement(2000)
        val workerB = asyncActorDecrement(100)
        workerA.await()
        workerB.await()
        println("actorCount $actorCount")

    }
    println("actor took $time")

    time = measureTimeMillis {
        val bufferedActor = actor<String>(capacity = 10) {
            for (msg in channel){
                println(msg)
            }
        }

        for (i in 1..30){
            bufferedActor.send(i.toString())
        }
        bufferedActor.close()
    }
    println("buffered actor took $time")

    time = measureTimeMillis {
        val dis = newFixedThreadPoolContext(3, "pool")
        val poolActor = actor<String>(dis) {
            for (msg in channel){
                println("Running in ${Thread.currentThread().name}")
            }
        }

        for (i in 1..30){
            poolActor.send(i.toString())
        }
        poolActor.close()
    }
    println(" actor took $time")

    time = measureTimeMillis {
        val workerA = mutexIncrement(2000)
        val workerB = mutexIncrement(500)
        workerA.await()
        workerB.await()
        println("mutexCount $mutexCounter")
    }
    println(" mutex took $time")
}

var mutex = Mutex()
var mutexCounter = 0

val actorContext = newSingleThreadContext("counterActor")
var actorCount = 0
var actorCounter = CoroutineScope(actorContext).actor<Action>{
    for (msg in channel){
        when(msg){
            Action.INCREASE -> actorCount++
            Action.DECREASE -> actorCount--
        }
    }
}

fun mutexIncrement(by: Int) = CoroutineScope(Dispatchers.Default).async {
    for (i in 0 until by){
        mutex.withLock {
            mutexCounter++
        }
    }
}

fun asyncActorIncrement(by: Int) = CoroutineScope(Dispatchers.Default).async{
    for (i in 0 until by){
        actorCounter.send(Action.INCREASE)
    }
}

fun asyncActorDecrement(by: Int) = CoroutineScope(Dispatchers.Default).async {
    for (i in 0 until by){
        actorCounter.send(Action.DECREASE)
    }
}


suspend fun createCoroutines(amount: Int){
    val jobs = ArrayList<Job>()
    for (i in 1..amount){
        jobs += GlobalScope.launch(Dispatchers.IO) {
            println("Started $i in ${Thread.currentThread().name}")
            delay(1000)
            println("Finished $i in ${Thread.currentThread().name}")
        }
    }
    jobs.forEach{// 모든 Job 종료 대기.ㅈ
        it.join()
    }
}

fun asyncGetUserInfo(id: Int) = GlobalScope.async {
    // delay(1100) : 추가 시 init error 발생.
    user = UserInfo(id = id, name = "JS", lastName = "Kim")
}

