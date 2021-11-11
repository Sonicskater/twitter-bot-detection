import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MoreThan50Following implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getNeighbor()).getFollowing().size() > 50;
    }
}
