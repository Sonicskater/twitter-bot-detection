package classifiers

import features.User

interface Classifier {

    fun classify(user: User) : Result

    data class Result(val bot: Double, val not_bot: Double){
        fun isBot() : Boolean{
            return bot >= not_bot
        }

    }
}