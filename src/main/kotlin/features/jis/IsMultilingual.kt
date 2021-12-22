package features.jis

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetector
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import features.BinaryFeature
import features.User
import smile.nlp.normalizer.SimpleNormalizer

class IsMultilingual : BinaryFeature {

    val detector = LanguageDetectorBuilder.fromAllSpokenLanguages().build()

    val langs : MutableMap<Language,LanguageDetector> = mutableMapOf()

    override fun hasFeature(user: User): Boolean {

        val data = user.tweets?.asSequence()?.map {
            it.normalized
        } ?: return false

        val tweetLang = data
            // makes mapping lazy, to speed up the program and prevent running expensive
            // all lang check on every single tweet
            .map {
                val x = detector.detectLanguageOf(it)
                //println("Detected Language: ${x.name}")
                x
            }.firstOrNull { lang ->
                lang != Language.UNKNOWN
            } ?: return false

        //println("Locked Language: ${tweetLang.name}")

//         cache the detectors to prevent huge memory pressure
        val lockedOn = langs.getOrPut(tweetLang){
            LanguageDetectorBuilder.fromLanguages(tweetLang, Language.UNKNOWN, Language.LATIN).build()
        }
//        val lockedOn = detector
        return data.any {
            lockedOn.detectLanguageOf(it) != tweetLang
        }
    }
}