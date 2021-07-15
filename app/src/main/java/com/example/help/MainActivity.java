package com.example.help;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.help.Fragments.HomeFragment;
import com.example.help.Fragments.SettingsFragment;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;


public class MainActivity extends AppCompatActivity {


    //Functionality of the bottom nav
    private final BubbleNavigationChangeListener bottom_nav_listener = new BubbleNavigationChangeListener() {
        @Override
        public void onNavigationChanged(View view, int position) {
            Fragment fragment = null;

            switch (position) {
                case 0:

                    fragment = new HomeFragment();
                    Log.d("TAG", "Home Pressed ");
                    // Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    break;
                case 1:

                    fragment = new SettingsFragment();
                    Log.d("TAG", "Setting Pressed ");

                    // Toast.makeText(MainActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        }
    };





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Third Party Bottom Navigation

        BubbleNavigationLinearView bottom_nav = (BubbleNavigationLinearView) findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        bottom_nav.setNavigationChangeListener(bottom_nav_listener);


    }


}