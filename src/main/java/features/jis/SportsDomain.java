package features.jis;

import features.BinaryFeature;
import features.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//Class to check if the number of accounts a user is following is significantly higher than the number of accounts following the user
public class SportsDomain implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        List<String> domains = user.getDomain();
        for(String domain : domains)
        {
            if(domain.equals("Sports"))
            {
                return true;
            }
        }
        return false;
    }
}