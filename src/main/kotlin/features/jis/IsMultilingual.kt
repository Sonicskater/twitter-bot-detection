package features.jis

import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import features.BinaryFeature
import features.User

class IsMultilingual : BinaryFeature {

    val detector = LanguageDetectorBuilder.fromAllLanguagesWithLatinScript().build()

    override fun hasFeature(user: User): Boolean {
        val langs = user.tweets?.map {
            detector.detectLanguageOf(it.text)
        }
        val first = langs?.first()

        return langs?.any { it != first } ?: false
    }
}