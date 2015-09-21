package com.jgrife.configdrawer.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.jgrife.configdrawer.R;

public class ConfigDrawerView extends FrameLayout {

    static ConfigDrawerView mConfigDrawerView;
    static OnConfigDrawerListener mConfigDrawerListener;
    DrawerLayout mDrawerLayout;
    ProgressBar mDrawerProgressBar;

    public ConfigDrawerView(Activity context) {
        super(context);
        init(context);
    }

    public ConfigDrawerView(Activity context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ConfigDrawerView(Activity context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Activity activity) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.config_drawer, null));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.config_drawer_drawerlayout);
        mConfigDrawerView = this;
        mDrawerProgressBar = (ProgressBar) findViewById(R.id.config_drawer_progress_bar);

        activity.getFragmentManager()
                .beginTransaction()
                .replace(R.id.config_drawer_framelayout, new ConfigDrawerFragment())
                .commit();
    }

    public static class ConfigDrawerFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.config_drawer);
            if (mConfigDrawerListener != null) {
                mConfigDrawerListener.onPreferencesLoaded(getPreferenceManager(), mConfigDrawerView);
            }

            for (int x = 0; x < getPreferenceScreen().getPreferenceCount(); x++) {
                PreferenceCategory preferenceCateg = (PreferenceCategory) getPreferenceScreen().getPreference(x);
                final Handler handler = new Handler(Looper.getMainLooper());
                for (int y = 0; y < preferenceCateg.getPreferenceCount(); y++) {
                    Preference pref = preferenceCateg.getPreference(y);
                    pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(final Preference preference) {
                            if (mConfigDrawerListener != null) {
                                /**
                                 *  NOTE run-loop on main thread. The run() function below will run after the
                                 *  PreferenceClick has been processed.
                                 */
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mConfigDrawerListener.onPreferenceClick(preference, mConfigDrawerView);
                                    }
                                });
                            }
                            return true;
                        }
                    });
                    pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                            if (mConfigDrawerListener != null) {
                                /**
                                 *  NOTE run-loop on main thread. The run() function below will run after the
                                 *  onPreferenceChange has been processed, ensuring that newValue has been saved to
                                 *  the SharedPreferences already and that preference object reflects the newValue.
                                 */
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mConfigDrawerListener.onPreferenceChange(preference, mConfigDrawerView);
                                    }
                                });
                            }
                            return true;
                        }
                    });
                }
            }
        }
    }

    /**
     * Adds the Config Drawer to the speficied {@link Activity}.  If the {@link Activity} layout does not contain a
     * {@link DrawerLayout}, the entire {@link ConfigDrawerView} is injected into the {@link Activity} layout.  If the
     * layout does contain a {@link DrawerLayout}, only the {@link android.support.design.widget.NavigationView} from
     * {@link R.layout#config_drawer} is injected into the {@link Activity} layout.
     *
     * @param activity the Activity instance that will be wrapped the Config Drawer.
     */
    public void wrapInside(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);

        if (parent.getChildAt(0) instanceof DrawerLayout) {
            ViewGroup content = (ViewGroup) parent.getChildAt(0);
            View nv = mDrawerLayout.findViewById(R.id.config_drawer_navigation_view);
            mDrawerLayout.removeViewAt(0);
            content.addView(nv);
        } else {
            View content = parent.getChildAt(0);
            parent.removeViewAt(0);
            mDrawerLayout.addView(content, 0);
            parent.addView(this);
        }
    }

    public void setConfigDrawerListener(OnConfigDrawerListener listener) {
        mConfigDrawerListener = listener;
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public void showProgressBar(boolean show) {
        mDrawerProgressBar.setVisibility(show ? VISIBLE : GONE);
    }

    public interface OnConfigDrawerListener {

        /**
         * Called when a Preference has been changed by the user. This is called AFTER the state of the Preference has
         * been updated and persisted.
         */
        void onPreferenceChange(Preference updatedPreference, ConfigDrawerView configDrawerView);

        /**
         * Called when a Preference has been clicked
         */
        void onPreferenceClick(Preference preference, ConfigDrawerView configDrawerViewr);

        /**
         * Called when the Preferences have been loaded into the Activity.
         */
        void onPreferencesLoaded(PreferenceManager preferenceManager, ConfigDrawerView configDrawerView);
    }
}
