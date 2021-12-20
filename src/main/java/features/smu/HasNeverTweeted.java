package features.smu;

import features.BinaryFeature;
import features.User;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//This is a class for the program to help check if the user has never tweeted.
public class HasNeverTweeted implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).getTweets() == 0;
    }
}
