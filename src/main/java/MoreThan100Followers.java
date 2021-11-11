import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MoreThan100Followers implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).getFollowers() > 100;
    }
}