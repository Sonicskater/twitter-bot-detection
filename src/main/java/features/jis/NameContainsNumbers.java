package features.jis;

import features.BinaryFeature;
import features.Profile;
import features.User;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//Classifier to check if the username has numbers, as numbers along with other bot made indicators, usually implies bot made user accounts most of the time.
public class NameContainsNumbers implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        Profile profile = Objects.requireNonNull(user.getProfile());
        String name = Objects.requireNonNull(profile.getName());
        return name.matches(".*\\d.*");
    }
}
