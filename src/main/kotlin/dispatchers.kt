import kotlinx.coroutines.*
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

/**
 * GlobalScope 클래스는 이제 @DelicateCoroutinesApi annotation 으로 표시됨.
 * 어플리케이션의 전체 수명 기간 동안 활성 상태로 유지되어야 하는 최상위 백그라운드 프로세스에 설정.
 */

@OptIn(DelicateCoroutinesApi::class)
val netDispatcher = newSingleThreadContext(name = "ServiceCall") // singleThread
val factory = DocumentBuilderFactory.newInstance()

fun main()= runBlocking {
    val task = launch(netDispatcher){
//        doSomething()
//        printcurrentThread()
        fetchRSSHeadlines()
    }
    task.join()
//    if(task.isCancelled){
//        val exception = task.getCancellationException()
//        println("Error with message : ${exception.cause}")
//    }else{
//        println("Success")
//    }
    println("Completed")
}

fun doSomething(){
    throw UnsupportedOperationException("Error")
}
fun printcurrentThread(){
    println("Running in thread ${Thread.currentThread().name}")
}
fun fetchRSSHeadlines(): List<String>{
    val builder = factory.newDocumentBuilder()
    val xml = builder.parse("https://www.npr.org/rss/rss.php?id=1001")
    val news = xml.getElementsByTagName("channel").item(0)
    return (0 until  news.childNodes.length)
        .map { news.childNodes.item(it) }
        .filter { Node.ELEMENT_NODE == it.nodeType }
        .map { it as Element }
        .filter { "item" == it.tagName }
        .map { it.getElementsByTagName("title").item(0).textContent }

}