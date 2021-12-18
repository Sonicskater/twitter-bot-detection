import org.jetbrains.kotlinx.dl.api.core.Sequential
import java.io.File

object BERT {

    private val config = File("bert/bert_config.json")
    private val weights = File("bert/bert_model.ckpt.data-00000-of-00001")

    val model by lazy {
        val x = Sequential.loadModelConfiguration(config)
        x.loadWeights(weights)
        x
    }
}