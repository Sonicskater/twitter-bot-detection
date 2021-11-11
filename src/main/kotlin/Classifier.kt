interface Classifier {

    fun classify(user: User) : Result

    data class Result(val bot: Double, val not_bot: Double)
}