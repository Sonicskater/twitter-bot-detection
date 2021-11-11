import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HasLocation implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).getHasLocation();
    }
}
