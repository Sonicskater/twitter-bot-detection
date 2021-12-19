package features.jis;

import features.BinaryFeature;
import features.Profile;
import features.User;
import org.jetbrains.annotations.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Objects;

public class NameContainsSymbols implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        Profile profile = Objects.requireNonNull(user.getProfile());
        String name = Objects.requireNonNull(profile.getName());

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        return m.find();
    }
}
