package features.jis;

import features.BinaryFeature;
import features.User;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

//Class to check if the number of accounts a user is following is significantly higher than the number of accounts following the user
public class HighFollowingToFollowersRatio implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        double following = Objects.requireNonNull(user.getProfile()).getFollowing();
        double followers = Objects.requireNonNull(user.getProfile()).getFollowers();
        return (following / followers) > 0.7;
    }
}