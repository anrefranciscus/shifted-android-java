package com.example.projectbootcamp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.projectbootcamp.R;
import com.example.projectbootcamp.utils.SharedPrefManager;
import com.example.projectbootcamp.utils.TabLayoutAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    TextView textName, textEmail;
    ImageView imageView;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    TabLayoutAdapter tabLayoutAdapter;
    String[] titles = new String[]{"Kuliner", "Gallery", "Review"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // shared pref
        sharedPrefManager = new SharedPrefManager(this);

        // Nav Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(
                getResources().getColor(R.color.white));

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Toast.makeText(getApplicationContext(),
                            "Menu Nav Home Selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    logOut();
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        View header = navigationView.getHeaderView(0);

        textName = header.findViewById(R.id.header_name);
        textEmail = header.findViewById(R.id.header_email);
        imageView = header.findViewById(R.id.imageView);
        textName.setText(sharedPrefManager.getSPNama());
        textEmail.setText(sharedPrefManager.getSPEmail());
        new GetReloadImageByUrl(imageView).execute(sharedPrefManager.getSPImage());
        Log.d("nama", "cek nama : " + sharedPrefManager.getSPNama());

        // Tabs Layout
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        tabLayoutAdapter = new TabLayoutAdapter(this);

        viewPager2.setAdapter(tabLayoutAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, ((tab, position) -> tab.setText(titles[position]))).attach();

        FloatingActionButton floatingActionButton = relativeLayout.findViewById(R.id.buttonFloating);

        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FormKuliner.class);
            startActivity(intent);
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private class GetReloadImageByUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public GetReloadImageByUrl(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageUrl).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("error", "doInBackground: " + e);
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    private void logOut() {
        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_STATUS_LOGIN, false);
        sharedPrefManager.removeData();
        startActivity(new Intent(MainActivity.this, LoginAcitivty.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}