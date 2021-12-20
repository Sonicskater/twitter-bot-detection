package features.jis;

import features.BinaryFeature;
import features.User;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//This is a class for the program to help check if a user is following more than 50 users.
public class MoreThan50Following implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).getFollowing() > 50;
    }
}
