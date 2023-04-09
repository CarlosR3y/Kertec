package com.example.kertec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    FloatingActionButton btn_fab;
    TabLayout tabLayout;

    Toolbar topAppBar;

    CoordinatorLayout startLayout;

    int[] colorIntArray = {R.color.md_theme_light_primaryContainer, R.color.md_theme_light_primaryContainer};
    int[] iconIntArray = {R.drawable.ic_add,R.drawable.ic_print};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btn_fab = findViewById(R.id.btn_fab);
        tabLayout = findViewById(R.id.tabLayout);
        topAppBar = findViewById(R.id.topAppBar);
        startLayout = findViewById(R.id.startLayout);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                animatedFab(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu:
                        FirebaseAuth.getInstance().signOut();
                        Snackbar.make(startLayout, "Usuario creado con exito", Snackbar.LENGTH_LONG).show();
                        goLogin();

                }
                return false;
            }
        });




        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = tabLayout.getSelectedTabPosition();

                switch (position){
                    case 0:
                        Intent intent = new Intent(StartActivity.this, OrdenActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intentLectura = new Intent(StartActivity.this, LecturaActivity.class);
                        startActivity(intentLectura);
                        break;
                }

            }
        });
    }

    private void goLogin() {
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    protected void animatedFab(final int position){
        btn_fab.clearAnimation();
        ScaleAnimation shrink =  new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btn_fab.setBackgroundTintList(getResources().getColorStateList(colorIntArray[position]));
                btn_fab.setImageDrawable(getResources().getDrawable(iconIntArray[position], null));

                // Scale up animation
                ScaleAnimation expand =  new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                btn_fab.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        btn_fab.startAnimation(shrink);
    }
}