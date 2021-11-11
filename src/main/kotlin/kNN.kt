class kNN(val k : Int = 3, val distance : (Array<Float>, Array<Float>) -> Float = EuclideanDistance) : Classifier{

    val features : List<LinearFeature> = listOf(LessThan30Followers().asLinear(),LevenshtienDistanceLessThan30().asLinear())

    private fun extractFeatures(user: User) : Array<Float>{
        return features.map {
            it.hasFeature(user)
        }
            .toTypedArray()
    }

    val knownFeatures = Datasets.train.data.map { user ->
        user to extractFeatures(user)
    }

    override fun classify(user: User): Classifier.Result {
        val userFeatures = extractFeatures(user)

        //println(userFeatures.toList())

        val distances = knownFeatures.map { (user, features) ->

            user to distance(userFeatures, features)
        }

        val result = distances.sortedBy { it.second }.take(k)

        val bots = result.count { (user, _) -> user.isBot() }
        val real = result.count { (user, _) -> !user.isBot() }

        return Classifier.Result(bots.toDouble()/k,real.toDouble()/k)
    }
}
