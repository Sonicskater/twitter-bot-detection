package features.jis;

import features.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentsRatio implements LinearFeature {

    @Override
    public float hasFeature(@NotNull User user) {
        List<Tweet> tweets = user.getTweets();
        if(tweets == null)
        {
            return 0;
        }

        int numTweets = 0;
        int numReplies = 0;
        for(Tweet tweet : tweets)
        {
            numTweets++;
            if(tweet instanceof Reply)
            {
                numReplies++;
            }
        }
        return (float)numReplies / numTweets;
    }
}

