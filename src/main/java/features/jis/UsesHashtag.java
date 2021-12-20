package features.jis;

import features.BinaryFeature;
import features.Tweet;
import features.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UsesHashtag implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user){
        List<Tweet> tweets = user.getTweets();
        if(tweets == null)
        {
            return false;
        }
        for(Tweet tweet : tweets)
        {
            String text = tweet.getText();
            String[] textarray = text.split("\\s+");
            for(String str : textarray)
            {
                if(str.startsWith("#"))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
