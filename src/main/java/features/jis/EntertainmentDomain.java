package features.jis;

import features.BinaryFeature;
import features.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntertainmentDomain implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        List<String> domains = user.getDomain();
        for(String domain : domains)
        {
            if(domain.equals("Entertainment"))
            {
                return true;
            }
        }
        return false;
    }
}