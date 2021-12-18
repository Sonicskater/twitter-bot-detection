import features.User
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.util.zip.ZipFile

object Datasets {
    private val filename = "Twibot-20.zip"

    private val stream by lazy { ZipFile(filename) }

    private val entries by lazy {
        stream.entries()
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
                    isLenient = true
                }.decodeFromStream<List<User>>(stream.getInputStream(it))


                Entry(it, data)
            }.toList()
    }

    val train = entries.find {
        it.z.name == "Twibot-20/train.json"
    }!!

    val support = entries.find {
        it.z.name == "Twibot-20/support.json"
    }!!

    val dev = entries.find {
        it.z.name == "Twibot-20/dev.json"
    }!!

    val test = entries.find {
        it.z.name == "Twibot-20/test.json"
    }!!
}