import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.util.zip.*

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) {

    val filename = "Twibot-20.zip"

    val stream = ZipFile(filename)

    val entries = stream.entries()
        .asSequence()
        .filter {
            !it.isDirectory
        }
        .filter {
            it.name.endsWith(".json")
        }
        .map {
            val data = Json{
                ignoreUnknownKeys = true
            }.decodeFromStream<List<User>>(stream.getInputStream(it))


            Entry(it, data)
        }.toList()



    println("ENTRIES: ${entries.map { it.data.size }}")

    val train = entries.find {
        it.z.name == "Twibot-20/train.json"
    }

    val support = entries.find {
        it.z.name == "Twibot-20/support.json"
    }

    val dev = entries.find {
        it.z.name == "Twibot-20/dev.json"
    }

    val test = entries.find {
        it.z.name == "Twibot-20/test.json"
    }


}

data class Entry(
    val z: ZipEntry,
    val data: List<User>
)