package features;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

//Classifier to check if the user account has more than a 100 likes.
public class LessThan100Likes implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return Objects.requireNonNull(user.getProfile()).getLikes() < 100;
    }
}