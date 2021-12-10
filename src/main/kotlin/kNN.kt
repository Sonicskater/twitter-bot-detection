class kNN(val k : Int = 3, val distance : (Array<Double>, Array<Double>) -> Double = EuclideanDistance) : Classifier{

    val features : List<LinearFeature> = listOf(
        AgeLessThan2Months().asLinear(),
        HasDescription().asLinear(),
        HasLocation().asLinear(),
        HasProfileLocation().asLinear(),
        LessThan30Followers().asLinear(),
        LevenshtienDistanceLessThan30().asLinear(),
        HighFollowingToFollowersRatio().asLinear(),
        LessThan100Likes().asLinear(),
        MoreThan50Following().asLinear(),
        MoreThan100Followers().asLinear(),
        UserIsVerified().asLinear(),
        ProfileUsesBackgroundImage().asLinear(),
        UsingDefaultProfile().asLinear(),
        UsingDefaultProfileImage().asLinear(),
    )

    private fun extractFeatures(user: User) : Array<Double>{
        return features.map {
            it.hasFeature(user).toDouble()
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
