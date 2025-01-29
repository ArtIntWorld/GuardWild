package com.example.guardwild;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guardwild.databinding.ActivityUserHomeBinding;

public class UserHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
private ActivityUserHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityUserHomeBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarUserHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_user_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_user_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.nav_home){
            Intent i=new Intent(getApplicationContext(),UserHome.class);
            startActivity(i);
        } else if(id==R.id.nav_complaint){
            Intent i = new Intent(getApplicationContext(), SendComplaint.class);
            startActivity(i);
        } else if(id==R.id.nav_profile){
            Intent i = new Intent(getApplicationContext(), UserProfile.class);
            startActivity(i);
        }else if(id==R.id.nav_animallist) {
            Intent i = new Intent(getApplicationContext(), AnimalList.class);
            startActivity(i);
        }else if(id==R.id.nav_station) {
            Intent i = new Intent(getApplicationContext(), StationDetails.class);
            startActivity(i);
        }else if(id==R.id.nav_alert) {
            Intent i = new Intent(getApplicationContext(), AlertList.class);
            startActivity(i);
        } else if(id==R.id.nav_logout) {
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
        }
        return true;
    }
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

}