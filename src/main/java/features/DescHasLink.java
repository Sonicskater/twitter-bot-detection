package features;

import features.BinaryFeature;
import features.User;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DescHasLink implements BinaryFeature {
    @Override
    public boolean hasFeature(@NotNull User user) {
        String desc = Objects.requireNonNull(user.getProfile()).getDescription();
        assert desc != null;
        return desc.contains("https://");
    }
}
