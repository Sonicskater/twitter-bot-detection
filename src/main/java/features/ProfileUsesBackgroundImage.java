package features;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//Class which helps check if the user has set a background image for their profile.
public class ProfileUsesBackgroundImage implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).getUsesBackgroundImage();
    }
}
