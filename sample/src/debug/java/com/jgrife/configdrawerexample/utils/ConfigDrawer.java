package com.jgrife.configdrawerexample.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.jgrife.configdrawerexample.BuildConfig;
import com.jgrife.configdrawerexample.R;
import com.jgrife.configdrawer.views.ConfigDrawerView;

import static com.jgrife.configdrawerexample.ConfigDrawerExampleApp.getAppContext;

/**
 * Utility class for handling the {@link ConfigDrawerView}. This utility class is only implemented for the Debug
 * build variant. The Release build variant has just a stubbed version of this class.
 */
public final class ConfigDrawer {

    private ConfigDrawer() {
        //not called
    }

    /**
     * Loads up the {@link ConfigDrawerView} and wraps it inside of the Activity.
     *
     * @param activity the Activity that you want the {@link ConfigDrawerView} wrapped inside of.
     */
    public static void loadConfigDrawer(Activity activity) {
        final ConfigDrawerView configDrawerView = new ConfigDrawerView(activity);
        configDrawerView.wrapInside(activity);
        configDrawerView.setConfigDrawerListener(new ConfigDrawerListener());
    }

    /**
     * Gets the string value of the key set by the {@link ConfigDrawerView}. {@link ConfigDrawerView} is a {@link
     * android.preference.PreferenceFragment}, so all value set by it are stored in the {@link SharedPreferences}.
     */
    public static String getConfigDrawerValue(String key) {
        // if the config drawer hasn't been loaded yet, we still want values from it, so setting the default values.
        PreferenceManager.setDefaultValues(getAppContext(), R.xml.config_drawer, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getAppContext());
        return sharedPreferences.getString(key, null);
    }

    /**
     * Gets the boolean value of the key set by the {@link ConfigDrawerView}. {@link ConfigDrawerView} is a {@link
     * android.preference.PreferenceFragment}, so all value set by it are stored in the {@link SharedPreferences}.
     */
    public static Boolean getConfigDrawerBoolValue(String key) {
        // if the config drawer hasn't been loaded yet, we still want values from it, so setting the default values.
        PreferenceManager.setDefaultValues(getAppContext(), R.xml.config_drawer, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getAppContext());
        return sharedPreferences.getBoolean(key, false);
    }

    static class ConfigDrawerListener implements ConfigDrawerView.OnConfigDrawerListener {

        @Override
        public void onPreferenceChange(Preference updatedPreference, ConfigDrawerView configDrawerView) {
            switch (updatedPreference.getKey()) {
                case "config_drawer_network_env":
                    Toast.makeText(
                            getAppContext(),
                            String.format("Selected Environment: %s", getConfigDrawerValue(updatedPreference.getKey())),
                            Toast.LENGTH_SHORT).show();
                    break;
                case "config_drawer_enable_crashlytics":
                    Toast.makeText(
                            getAppContext(),
                            String.format("Go ahead and enable Crashlytics: %b", getConfigDrawerBoolValue(updatedPreference.getKey())),
                            Toast.LENGTH_SHORT).show();
                default:
                    // no action, may be missing the case check for a preference.getKey()
            }
        }

        @Override
        public void onPreferenceClick(Preference preference, ConfigDrawerView configDrawerView) {
            switch (preference.getKey()) {
                case "config_drawer_logout":
                    Toast.makeText(getAppContext(), "Time to log the user out", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    // no action, may be missing the case check for a preference.getKey()
            }
        }

        @Override
        public void onPreferencesLoaded(PreferenceManager preferenceManager, ConfigDrawerView configDrawerView) {
            preferenceManager.findPreference("config_drawer_app_build")
                    .setSummary(String.format("BuildType: %s \nVersionName: %s \nVersionCode: %s",
                            BuildConfig.BUILD_TYPE, BuildConfig.VERSION_NAME,
                            BuildConfig.VERSION_CODE));
        }
    }
}
