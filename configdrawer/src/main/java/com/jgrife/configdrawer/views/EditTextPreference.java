package com.jgrife.configdrawer.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.jgrife.configdrawer.R;

/**
 * Customized {@link android.preference.EditTextPreference} that retrieves the summary for displaying
 * on the preference screen.
 */
public class EditTextPreference extends android.preference.EditTextPreference {
    private String mSummary;

    public EditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        /* Retrieve the Preference summary attribute since it's private
         * in the Preference class.
         */
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Custom_View, defStyleAttr, 0);
        mSummary = a.getString(R.styleable.Custom_View_android_summary);
        a.recycle();
    }

    public EditTextPreference(Context context, AttributeSet attrs) {
        this(context, attrs, Resources.getSystem().getIdentifier("editTextPreferenceStyle", "attr", "android"));
    }

    public EditTextPreference(Context context) {
        this(context, null);
    }

    /**
     * Returns the summary of this ListPreference. If the summary
     * has a {@linkplain java.lang.String#format String formatting}
     * marker in it (i.e. "%s" or "%1$s"), then the current entry
     * value will be substituted in its place.
     *
     * @return the summary with appropriate string substitution
     */
    @Override
    public CharSequence getSummary() {
        final CharSequence entry = getText();
        if (mSummary == null) {
            return super.getSummary();
        } else {
            return String.format(mSummary, entry == null ? "" : entry);
        }
    }
}
