package tm.fantom.simplemqtt.util;

import android.text.format.DateUtils;

import java.util.Calendar;

public final class Utils {

    public static String timeMillisToAgo(long millis){
       return DateUtils.getRelativeTimeSpanString(millis , Calendar.getInstance().getTimeInMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    }
}
