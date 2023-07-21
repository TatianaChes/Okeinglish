package com.example.myapplicationtest.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplicationtest.R;
import com.example.myapplicationtest.input.CheckEnter;
import com.example.myapplicationtest.input.SplashActivity;
import com.example.myapplicationtest.study.StudyActivity;
import com.example.myapplicationtest.ui.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplicationtest.databinding.ActivityMenuBinding;
import com.orhanobut.hawk.Hawk;

public class MenuActivity extends AppCompatActivity {

private ActivityMenuBinding binding;
    Fragment mFragment = null;
    boolean status = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Hawk.init(this).build();
        binding = ActivityMenuBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        try {
            if (Hawk.get("key").equals("Admin")) {
                navView.getMenu().findItem(R.id.navigation_profile).setIcon(R.drawable.ic_baseline_check_circle_24);
                navView.getMenu().findItem(R.id.navigation_profile).setTitle("Study");
            }
        } catch (Exception e){}

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
               .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_menu);

    //    NavigationUI.setupWithNavController(binding.navView, navController);

        // запуск начального экрана
        mFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_menu, mFragment).commit();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        item.setChecked(true);
                        navController.navigate(R.id.navigation_home);
                        fragmentManager.beginTransaction()
                                .remove(mFragment).commit();
                        break;
                    case R.id.navigation_texts:
                        item.setChecked(true);
                        navController.navigate(R.id.navigation_notifications);
                        fragmentManager.beginTransaction()
                                .remove(mFragment).commit();
                        break;

                    case R.id.navigation_rules:
                        item.setChecked(true);
                        navController.navigate(R.id.navigation_dashboard);
                        fragmentManager.beginTransaction()
                                .remove(mFragment).commit();
                        break;

                    case R.id.navigation_profile:
                        item.setChecked(true);
                        if(Hawk.get("key") != null && Hawk.get("key").equals("Admin")) {
                                Intent intent = new Intent(MenuActivity.this, StudyActivity.class);
                                startActivity(intent);
                            }
                      else {
                            Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                        break;

                }
                return false;
            }
        });


    }
   @Override
    public void onRestart() {
        super.onRestart();
        if(CheckEnter.update) {
            Intent intent = new Intent(MenuActivity.this, SplashActivity.class);
            startActivity(intent);
            CheckEnter.update = true;
            //finish();
        }
       CheckEnter.update = true;
    }

 @Override
    public  void onDestroy(){
     super.onDestroy();
    }
 }