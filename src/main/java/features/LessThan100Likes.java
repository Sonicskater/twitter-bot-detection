package features;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class LessThan100Likes implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).getLikes() < 100;
    }
}