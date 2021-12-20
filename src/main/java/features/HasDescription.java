package features;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//Classifier which checks if the user account has a custom profile description.
public class HasDescription implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).getHasDescription();
    }
}
