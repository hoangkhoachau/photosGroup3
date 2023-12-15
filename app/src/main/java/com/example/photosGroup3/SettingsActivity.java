package com.example.photosGroup3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

public class SettingsActivity extends Activity {

    boolean isPasswordSet = false;
    String savedPass;
    String savedNumber;

    // TODO: Rename and change types of parameters

    LinearLayout privateAlbum;

    SharedPreferences sharePrf;
    SharedPreferences.Editor edit;

    SwitchCompat changeDark;

    public SettingsActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        boolean status = MainActivity.mainActivity.getIsDark();
        if (status) {
AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        privateAlbum = findViewById(R.id.album_private);
        privateAlbum.setVisibility(View.GONE);
        changeDark = findViewById(R.id.switchDarkmode);

        changeDark.setChecked(status);
        changeDark.setOnCheckedChangeListener((compoundButton, isDark) -> {
            MainActivity.mainActivity.setIsDark(isDark);
            new Handler().postDelayed(() -> recreate(), 100);
        });

        sharePrf = MainActivity.mainActivity.getSharedPreferences("AppPreferences", MODE_PRIVATE);
        edit = sharePrf.edit();
        isPasswordSet = sharePrf.getBoolean("pass_set", false);
        savedPass = sharePrf.getString("password","");
        savedNumber = sharePrf.getString("number_phone", "");
    }


    private void showPrivateAlbum(){
        MainActivity.mainActivity.viewPager2.setCurrentItem(1);
//        ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction()
//                .replace(R.id.viewPager, AlbumDisplayFragment.newInstance(albumList.get(0)), null)
//                .setReorderingAllowed(true)
//                .commit();
    }


}