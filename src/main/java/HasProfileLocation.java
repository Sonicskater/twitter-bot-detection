import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HasProfileLocation implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).getHasProfileLocation();
    }
}
