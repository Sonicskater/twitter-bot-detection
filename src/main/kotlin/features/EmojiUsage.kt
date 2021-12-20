package features

import kotlin.text.Regex.Companion.escape


class EmojiUsage() : LinearFeature{
    override fun hasFeature(user: User): Float {
        if (user.tweets == null){
            return 0.0f
        }
        return user.tweets!!.count {
            it.text.hasEmoji()
        }.toFloat().div(user.tweets!!.size.toFloat())
    }
}

private val regex = Regex("(\\u00a9|\\u00ae|[\\u2000-\\u3300]|\\ud83c[\\ud000-\\udfff]|\\ud83d[\\ud000-\\udfff]|\\ud83e[\\ud000-\\udfff])")

fun String.hasEmoji() : Boolean{
    return this.contains(regex)
}