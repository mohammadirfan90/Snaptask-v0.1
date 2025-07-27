package com.example.snaptask_v01;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        bottomNav = findViewById(R.id.bottom_navigation);
//        bottomNav.setOnNavigationItemSelectedListener(navListener);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
//    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
//            item -> {
//                Fragment selectedFragment = null;
//
//                switch (item.getItemId()) {
//                    case R.id.nav_home:
//                        selectedFragment = new HomeFragment();
//                        break;
//                    case R.id.nav_post:
//                        selectedFragment = new PostFragment();
//                        break;
//                    case R.id.nav_messages:
//                        selectedFragment = new MessageFragment();
//                        break;
//                    case R.id.nav_notifications:
//                        selectedFragment = new NotificationFragment();
//                        break;
//                    case R.id.nav_profile:
//                        selectedFragment = new ProfileFragment();
//                        break;
//                }
//
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, selectedFragment).commit();
//
//                return true;
//            };
}
