package com.example.snaptask_v01;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.snaptask_v01.fragment.ChatListFragment;
import com.example.snaptask_v01.fragment.ClientHomeFragment;
import com.example.snaptask_v01.fragment.ClientProfileFragment;
import com.example.snaptask_v01.fragment.FreelancerHomeFragment;
import com.example.snaptask_v01.fragment.FreelancerOrdersFragment;
import com.example.snaptask_v01.fragment.FreelancerProfileFragment;
import com.example.snaptask_v01.fragment.GigListFragment;
import com.example.snaptask_v01.fragment.SearchGigFragment;
import com.example.snaptask_v01.utils.AppSessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
//    private FirebaseAuth auth;
//    MaterialButton buttonSignOut;
//    private BottomNavigationView bottomNav;
    BottomNavigationView bnView;
    String activeRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize session manager
        AppSessionManager.init(getApplicationContext());

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bnView = findViewById(R.id.bnView);

        // Step 1: Get role from Firebase or SessionManager
        activeRole = AppSessionManager.getInstance().getActiveRole();

        // Step 2: Load correct menu
        if ("freelancer".equals(activeRole)) {
            bnView.inflateMenu(R.menu.menu_freelancer);
            loadFragment(new FreelancerHomeFragment());
        } else {
            bnView.inflateMenu(R.menu.menu_client);
            loadFragment(new ClientHomeFragment());
        }


        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();

                if ("client".equals(activeRole)) {
                    if (id == R.id.nav_client_home) {
                        selectedFragment = new ClientHomeFragment();
                    } else if (id == R.id.nav_search_freelancers) {
                        selectedFragment = new SearchGigFragment();
                    } else if (id == R.id.nav_messages) {
                        selectedFragment = new ChatListFragment();
                    } else if (id == R.id.nav_client_profile) {
                        selectedFragment = new ClientProfileFragment();
                    }

                } else if ("freelancer".equals(activeRole)) {
                    if (id == R.id.nav_freelancer_home) {
                        selectedFragment = new FreelancerHomeFragment();
                    } else if (id == R.id.nav_gigs_list) {
                        selectedFragment = new GigListFragment();
                    } else if (id == R.id.nav_freelacer_orders) {
                        selectedFragment = new FreelancerOrdersFragment();
                    } else if (id == R.id.nav_messages) {
                        selectedFragment = new ChatListFragment();
                    } else if (id == R.id.nav_freelancer_profile) {
                        selectedFragment = new FreelancerProfileFragment();
                    }

                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    return true;
                }

                return false;
            }
        });

//        auth = FirebaseAuth.getInstance();
//        buttonSignOut = findViewById(R.id.buttonSignOut);
//        buttonSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

//        bottomNav = findViewById(R.id.bottom_navigation);
//        bottomNav.setOnNavigationItemSelectedListener(navListener);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

//    public void loadFragment(Fragment fragment, int flag) {
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        if (flag == 0) {
//            ft.add(R.id.container, fragment);
//        } else {
//            ft.replace(R.id.container, fragment);
//        }
//        ft.commit();
//    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
