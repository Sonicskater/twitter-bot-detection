package features.smu;

import features.BinaryFeature;
import features.User;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//Checks if the user account is missingID from the database.
public class MissingID implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        return !Objects.requireNonNull(user.getProfile()).getHasID();
    }
}
