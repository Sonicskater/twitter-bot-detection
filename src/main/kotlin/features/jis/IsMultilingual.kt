package features.jis

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import features.BinaryFeature
import features.User
import smile.nlp.normalizer.SimpleNormalizer

class IsMultilingual : BinaryFeature {

    val detector = LanguageDetectorBuilder.fromAllLanguagesWithLatinScript().build()

    val normalizer = SimpleNormalizer.getInstance()

    override fun hasFeature(user: User): Boolean {

        val data = user.tweets?.map {
            normalizer.normalize(it.text) // Normalize tweet text
        } ?: return false

        val tweetLang = data.map {
            val x = detector.detectLanguageOf(it)
            println("Detected Language: ${x.name}")
            x
        }.firstOrNull { lang ->
            lang != Language.UNKNOWN
        } ?: return false

        println("Locked Language: ${tweetLang.name}")

        val lockedOn = LanguageDetectorBuilder.fromLanguages(tweetLang, Language.UNKNOWN).build()

        return data.any {
            lockedOn.detectLanguageOf(it) != tweetLang
        }
    }
}