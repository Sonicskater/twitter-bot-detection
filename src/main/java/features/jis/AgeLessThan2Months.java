package features.jis;

import features.BinaryFeature;
import features.Profile;
import features.User;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

//This classifier is for checking if the user account is less than 2 months old, the date May 28, 2021 is currently used because of dataset used is not current.
public class AgeLessThan2Months implements BinaryFeature {

    @Override
    public boolean hasFeature(@NotNull User user) {
        Date d = new Date(2021, Calendar.MAY, 28);
        Profile p = Objects.requireNonNull(user.getProfile());
        Date created = p.getCreation_date();
        long diff = d.getTime() - Objects.requireNonNull(created).getTime();

        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) < 60;
    }
}
