package com.zohaib.smartattendancestudent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import com.zohaib.smartattendancestudent.fragments.FragAbout;
import com.zohaib.smartattendancestudent.fragments.FragHome;
import com.zohaib.smartattendancestudent.fragments.FragProfile;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SNavigationDrawer sNavigationDrawer;
    Class fragmentClass;
    public static Fragment fragment;
    public static String name, roll, contact, deviceId;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.students_pref), MODE_PRIVATE);
        name = sharedPreferences.getString(getString(R.string.key_name), "");
        roll = sharedPreferences.getString(getString(R.string.key_roll), "");
        contact = sharedPreferences.getString(getString(R.string.key_contact), "");
        deviceId = sharedPreferences.getString(getString(R.string.key_deviceId), "");
        Log.d("taaag", "deviceId= " + deviceId);
        Log.d("taaag", "name= " + name);
        Log.d("taaag", "roll= " + roll);
        Log.d("taaag", "contact= " + contact);
        if (deviceId.equals("")) {
            saveDeviceIdToSharedPref();
        }

        //Creating a list of menu Items

        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Home", R.drawable.news_bg));
        menuItems.add(new MenuItem("Profile", R.drawable.feed_bg));
        menuItems.add(new MenuItem("About", R.drawable.feed_bg));

        sNavigationDrawer.setMenuItemList(menuItems);
        fragmentClass = FragHome.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
        }

        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position " + position);

                switch (position) {
                    case 0: {
                        fragmentClass = FragHome.class;
                        break;
                    }
                    case 1: {
                        fragmentClass = FragProfile.class;
                        break;
                    }
                    case 2: {
                        fragmentClass = FragAbout.class;
                        break;
                    }

                }

                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerOpening() {

                    }

                    @Override
                    public void onDrawerClosing() {
                        System.out.println("Drawer closed");

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.students_pref), MODE_PRIVATE);
                            name = sharedPreferences.getString(getString(R.string.key_name), "");
                            roll = sharedPreferences.getString(getString(R.string.key_roll), "");
                            contact = sharedPreferences.getString(getString(R.string.key_contact), "");
                            deviceId = sharedPreferences.getString(getString(R.string.key_deviceId), "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (fragment != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();

                        }
                    }

                    @Override
                    public void onDrawerClosed() {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        System.out.println("State " + newState);
                    }
                });
            }
        });


    }

    private void saveDeviceIdToSharedPref() {
        Log.d("taaag", "getting deviceid");
        String deviceIdd = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        Log.d("taaag", "deviceId= " + deviceIdd);
        getSharedPreferences(getString(R.string.students_pref), MODE_PRIVATE).edit().putString(getString(R.string.key_deviceId), deviceIdd).apply();
        deviceId = deviceIdd;
    }


}