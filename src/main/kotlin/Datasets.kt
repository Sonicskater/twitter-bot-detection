import features.User
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.util.zip.ZipFile

object Datasets {
    private val filename = "Twibot-20.zip"

    private val entries by lazy {
        val stream = ZipFile(filename)
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

    val train = Dataset(entries.find {
        it.z.name == "Twibot-20/train.json"
    }!!.data)
        .also { dataset ->
            dataset.forEach {
                it.neighbor?.dataset = dataset
            }
        }

    val support = Dataset(entries.find {
        it.z.name == "Twibot-20/support.json"
    }!!.data)
        .also { dataset ->
            dataset.forEach {
                it.neighbor?.dataset = dataset
            }
        }

    val dev = Dataset(entries.find {
        it.z.name == "Twibot-20/dev.json"
    }!!.data)
        .also { dataset ->
            dataset.forEach {
                it.neighbor?.dataset = dataset
            }
        }


    val test = Dataset(entries.find {
        it.z.name == "Twibot-20/test.json"
    }!!.data)
        .also { dataset ->
            dataset.forEach {
                it.neighbor?.dataset = dataset
            }
        }

}

class Dataset(private val list : List<User>) : List<User> by list{

    val cache : MutableMap<String,User?> = mutableMapOf()

    fun findByID(id : String) : User?{
        return cache.getOrPut(id){
            list.find { it.ID == id }
        }
    }
}