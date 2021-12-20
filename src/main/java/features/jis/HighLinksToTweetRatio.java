package features.jis;

import features.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighLinksToTweetRatio implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        List<Tweet> tweets = user.getTweets();
        if(tweets == null)
        {
            return false;
        }

        int numTweets = 0;
        int numLinks = 0;
        for(Tweet tweet : tweets)
        {
            if(!(tweet instanceof Retweet))
            {
                numTweets++;
                String text = tweet.getText();
                String url = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
                Pattern pattern = Pattern.compile(url);
                Matcher matcher = pattern.matcher(text);

                if(matcher.find())
                {
                    numLinks++;
                }
            }
        }
        return (float)numTweets / numLinks > 0.8;
    }
}

