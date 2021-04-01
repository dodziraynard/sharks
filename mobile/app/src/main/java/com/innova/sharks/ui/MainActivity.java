package com.innova.sharks.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.innova.sharks.R;
import com.innova.sharks.adapter.TabAdapter;
import com.innova.sharks.databinding.ActivityMainBinding;
import com.innova.sharks.ui.explore.ExploreFragment;
import com.innova.sharks.ui.library.LibraryFragment;
import com.innova.sharks.ui.my_courses.MyCoursesFragment;
import com.innova.sharks.ui.quiz.MyQuizzesActivity;
import com.innova.sharks.ui.scholarship.ScholarshipFragment;
import com.innova.sharks.ui.start.StartActivity;
import com.innova.sharks.ui.update_profile.UpdateProfileActivity;
import com.innova.sharks.utils.Constants;

import static com.innova.sharks.utils.Constants.SHARED_PREFS_FILE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setTitle(getString(R.string.shark_app_name));

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        boolean isNew = prefs.getBoolean(Constants.IS_NEW_USER, true);
        if (isNew) {
            startActivity(new Intent(this, StartActivity.class));
            finish();
        }

        // Setting up tab layout.
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyCoursesFragment(), getString(R.string.my_courses_fragment_title));
        adapter.addFragment(new ExploreFragment(), getString(R.string.find_fragment_title));
        adapter.addFragment(new ScholarshipFragment(), getString(R.string.scholarship_fragment_title));
        adapter.addFragment(new LibraryFragment(), getString(R.string.library));
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_quiz) {
            startActivity(new Intent(this, MyQuizzesActivity.class));
        } else if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(this, UpdateProfileActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}