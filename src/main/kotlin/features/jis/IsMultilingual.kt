package features.jis

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import features.BinaryFeature
import features.User

class IsMultilingual : BinaryFeature {

    val detector = LanguageDetectorBuilder.fromLanguages(Language.ENGLISH, Language.HINDI, Language.RUSSIAN, Language.SPANISH, Language.FRENCH, Language.GERMAN).build()

    override fun hasFeature(user: User): Boolean {
        val firstTweet=user.tweets?.first() ?: return false
        val first = detector.detectLanguageOf(firstTweet.text)

        val checker = LanguageDetectorBuilder.fromLanguages(first, Language.LATIN).build()

        return user.tweets?.any { checker.detectLanguageOf(it.text) != first } ?: false
    }
}