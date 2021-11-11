import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.util.zip.*

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) {

    println("ENTRIES: ${ Datasets.dev.data.size }")
    println("TWEETS: ${Datasets.dev.data.sumOf { it.tweet?.size ?: 0 } }")

    println("BOTS: ${Datasets.dev.data.count { it.isBot() }} ")

    val runtime = Runtime.getRuntime()
    val usedMemory = runtime.totalMemory()-runtime.freeMemory()

    println("USING ${usedMemory/(1024*1024)} MEGABYTES")

}

data class Entry(
    val z: ZipEntry,
    val data: List<User>
)