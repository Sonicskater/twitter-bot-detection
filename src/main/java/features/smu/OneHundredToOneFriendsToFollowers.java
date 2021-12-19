package features.smu;

import features.BinaryFeature;
import features.User;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class OneHundredToOneFriendsToFollowers implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        double following = Objects.requireNonNull(user.getProfile()).getFollowing();
        double followers = Objects.requireNonNull(user.getProfile()).getFollowers();
        return (following / followers) >= 100;
    }
}