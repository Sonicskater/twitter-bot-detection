package features.smu;

import features.BinaryFeature;
import features.User;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//This is a class for the program to help check if a user has more than 1000 users following them.
public class MoreThan1kFollowers implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).getFollowers() > 1000;
    }
}