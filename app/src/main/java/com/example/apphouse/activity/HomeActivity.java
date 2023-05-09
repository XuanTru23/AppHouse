package com.example.apphouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.apphouse.R;
import com.example.apphouse.adapter.ViewPager2HomeAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager2;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        anhxa();

        bottomNavigationView.setBackground(null);
        ViewPager2HomeAdapter viewPager2HomeAdapter = new ViewPager2HomeAdapter(this);
        viewPager2.setAdapter(viewPager2HomeAdapter);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item_home:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.item_user:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.item_like:
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.item_setting:
                        viewPager2.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.item_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.item_user).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.item_like).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.item_setting).setChecked(true);
                        break;
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddHouseActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

    }
    private void anhxa(){
        bottomNavigationView = findViewById(R.id.bottom_nav_home);
        viewPager2 = findViewById(R.id.viewPager2_Home);
        floatingActionButton = findViewById(R.id.flb);
    }

}