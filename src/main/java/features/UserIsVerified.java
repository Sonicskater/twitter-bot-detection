package features;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//Class which helps check if the user is a verified account on twitter.
public class UserIsVerified implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).isVerified();
    }
}
