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
        changeDark = findViewById(R.id.switchDarkmode);

        changeDark.setChecked(status);
        changeDark.setOnCheckedChangeListener((compoundButton, isDark) -> {
            MainActivity.mainActivity.setIsDark(isDark);
            new Handler().postDelayed(() -> recreate(), 100);
        });

        privateAlbum.setOnClickListener(view1 -> {
            if (isPasswordSet){
                showInputPasswordDialog();
            } else {
                showSetPasswordDialog();
            }
        });

        sharePrf = MainActivity.mainActivity.getSharedPreferences("AppPreferences", MODE_PRIVATE);
        edit = sharePrf.edit();
        isPasswordSet = sharePrf.getBoolean("pass_set", false);
        savedPass = sharePrf.getString("password","");
        savedNumber = sharePrf.getString("number_phone", "");
    }

    private void showSetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.set_pass,null);
        Button cancelButton = dialogView.findViewById(R.id.set_pass_cancel_button);
        Button confirmButton = dialogView.findViewById(R.id.confirm_pass_button);
        EditText password = dialogView.findViewById(R.id.enter_set_pass);
        EditText confirmPassword = dialogView.findViewById(R.id.confirm_pass);
        EditText numberPhone = dialogView.findViewById(R.id.set_number_phone);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        confirmButton.setOnClickListener(v ->{
            String pass = password.getText().toString().trim();
            String cfpass = confirmPassword.getText().toString().trim();
            String num = numberPhone.getText().toString().trim();
            if (pass.length() < 5){
                Toast.makeText(this,"Password must have more than 4 characters", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(cfpass)){
                Toast.makeText(this,"Please enter the correct confirm-password", Toast.LENGTH_SHORT).show();
            } else if (num.length()!=10){
                Toast.makeText(this,"Please enter the correct your number phone", Toast.LENGTH_SHORT).show();
            } else {
                edit.putBoolean("pass_set", true);
                edit.putString("password", pass);
                edit.putString("number_phone", num);
                edit.apply();
                alertDialog.dismiss();
                MainActivity.mainActivity.recreate();
                Toast.makeText(this, "Set password success",Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void showInputPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.input_pass,null);
        Button okButton = dialogView.findViewById(R.id.ok_pass_button);
        EditText password = dialogView.findViewById(R.id.enter_input_pass);
        EditText numberPhone = dialogView.findViewById(R.id.input_number_phone);
        TextView forgotText = dialogView.findViewById(R.id.forgot_pass);

        forgotText.setOnClickListener(v -> {
            forgotText.setVisibility(View.INVISIBLE);
            dialogView.findViewById(R.id.forgot_layout).setVisibility(View.VISIBLE);
        });



        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        okButton.setOnClickListener(v->{
            String pass = password.getText().toString().trim();
            String num = numberPhone.getText().toString().trim();
            if (pass.equals(savedPass)){
                alertDialog.dismiss();
                showPrivateAlbum();
            } else if (num.equals(savedNumber)){
                alertDialog.dismiss();
                showPrivateAlbum();
            } else {
                if (dialogView.findViewById(R.id.forgot_layout).getVisibility() == View.INVISIBLE && !pass.equals(savedPass)){
                    Toast.makeText(this, "Please enter the correct password",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please enter the correct number phone",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showPrivateAlbum(){
        MainActivity.mainActivity.viewPager2.setCurrentItem(1);
//        ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction()
//                .replace(R.id.viewPager, AlbumDisplayFragment.newInstance(albumList.get(0)), null)
//                .setReorderingAllowed(true)
//                .commit();
    }


}