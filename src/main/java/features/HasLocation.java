package features;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//Class which helps check if the user account has set a custom location for their profile.
public class HasLocation implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).getHasLocation();
    }
}
