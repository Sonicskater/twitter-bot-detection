package features;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//Class which helps check if the user account has a default twitter profile image.
public class UsingDefaultProfileImage implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).isDefaultProfileImage();
    }
}
