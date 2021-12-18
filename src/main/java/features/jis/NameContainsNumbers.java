package features.jis;

import features.BinaryFeature;
import features.Profile;
import features.User;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NameContainsNumbers implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        Profile profile = Objects.requireNonNull(user.getProfile());
        String name = Objects.requireNonNull(profile.getName());
        return name.matches(".*\\d.*");
    }
}
