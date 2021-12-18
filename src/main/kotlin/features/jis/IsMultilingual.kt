package features.jis

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import features.BinaryFeature
import features.User
import smile.nlp.normalizer.SimpleNormalizer

class IsMultilingual : BinaryFeature {

    val detector = LanguageDetectorBuilder.fromLanguages(Language.ENGLISH, Language.HINDI, Language.RUSSIAN, Language.SPANISH, Language.FRENCH, Language.GERMAN).build()

    val normalizer = SimpleNormalizer.getInstance()

    override fun hasFeature(user: User): Boolean {

        val data = user.tweets?.map {
            normalizer.normalize(it.text) // Normalize tweet text
        } ?: return false

        val firstTweet= data.first()
        val first = detector.detectLanguageOf(firstTweet)

        val checker = LanguageDetectorBuilder.fromLanguages(first, Language.ENGLISH, Language.HINDI).build()

        return data.any { checker.detectLanguageOf(it) != first }
    }
}