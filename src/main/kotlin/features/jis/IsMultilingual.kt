package features.jis

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import features.BinaryFeature
import features.User

class IsMultilingual : BinaryFeature {

    val detector = LanguageDetectorBuilder.fromLanguages(Language.ENGLISH, Language.HINDI, Language.RUSSIAN, Language.SPANISH, Language.FRENCH, Language.GERMAN).build()

    override fun hasFeature(user: User): Boolean {
        val langs = user.tweets?.map {
            detector.detectLanguageOf(it.text)
        }
        val first = langs?.first()

        return langs?.any { it != first } ?: false
    }
}