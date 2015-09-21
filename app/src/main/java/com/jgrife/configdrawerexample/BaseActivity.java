package com.jgrife.configdrawerexample;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.jgrife.configdrawerexample.utils.ConfigDrawer;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        // Loads Config Drawer for Debug build. Release build does nothing.
        ConfigDrawer.loadConfigDrawer(this);
    }

}
