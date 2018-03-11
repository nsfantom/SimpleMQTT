package tm.fantom.simplemqtt.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nsfantom on 1/3/18.
 */

public final class PrefStorage {
    private final String TOKEN = "token";
    private final String COUNTRY_CODE = "country_code";

    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefStorage(Context context) {
        this.context = context;
    }

    public void saveToken(String token) {
        getPrefs().edit()
                .putString(TOKEN, token)
                .apply();
    }

    public String getToken() {
        return getPrefs().getString(TOKEN, "");
    }

    public void saveCountryCode(String countryCode) {
        getPrefs().edit()
                .putString(COUNTRY_CODE, countryCode)
                .apply();
    }

    public String getCountryCode() {
        return getPrefs().getString(COUNTRY_CODE, "");
    }

    SharedPreferences getPrefs() {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(PrefStorage.class.getSimpleName(), Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
