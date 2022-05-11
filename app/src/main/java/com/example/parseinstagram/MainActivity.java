package com.example.parseinstagram;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parseinstagram.fragment.ComposeFragment;
import com.example.parseinstagram.fragment.PostFragment;
import com.example.parseinstagram.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.nux_dayone_landing_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            Log.d("MainActivity", "Home Click");
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_Home:
                    Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                    fragment = new PostFragment();
                    break;
                case R.id.action_Compose:
                    Toast.makeText(MainActivity.this, "Compose", Toast.LENGTH_SHORT).show();
                    fragment = new ComposeFragment();
                    break;
                case R.id.action_Profile:
                default:
                    Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                    fragment = new ProfileFragment();
                    break;

            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.action_Home);
    }


}