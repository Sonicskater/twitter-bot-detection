package features.jis;

import features.LinearFeature;
import features.User;
import org.jetbrains.annotations.NotNull;

public class DomainCount implements LinearFeature {

    @Override
    public float hasFeature(@NotNull User user) {
        return (float)user.getDomain().size() / 4;
    }

    @NotNull
    @Override
    public String name() {
        return "DomainCount";
    }
}