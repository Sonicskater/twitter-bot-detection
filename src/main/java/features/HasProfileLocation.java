package features;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//Class which helps check if the user account has selected from a default list of locations offered by the site.
public class HasProfileLocation implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).getHasProfileLocation();
    }
}
