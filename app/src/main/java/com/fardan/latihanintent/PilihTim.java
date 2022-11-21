package com.fardan.latihanintent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.autofill.FieldClassification;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class PilihTim extends AppCompatActivity {

    private static final int HOME_REQUEST_CODE = 1;
    private static final int AWAY_REQUEST_CODE = 2;
    private static final String TAG = PilihTim.class.getCanonicalName();

    private String hometeam;
    private String awayteam;
    private EditText homeTeamInput;
    private EditText awayTeamInput;
    private ImageView homeLogo;
    private ImageView awayLogo;
    private Button buttonTeam;
    private Uri homeImg;
    private Uri awayImg;
    private boolean siapUbahImgHome = false;
    private boolean siapUbahImgAway = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_tim);

        homeTeamInput = findViewById(R.id.home_team);
        awayTeamInput = findViewById(R.id.away_team);
        homeLogo = findViewById(R.id.home_logo);
        awayLogo = findViewById(R.id.away_logo);
        buttonTeam = findViewById(R.id.btn_team);

        buttonTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent
                hometeam = homeTeamInput.getText().toString();
                awayteam = awayTeamInput.getText().toString();
                if (hometeam.isEmpty()) {
                    homeTeamInput.setError("Masukkan Nama Home Team");
                } else if (awayteam.isEmpty()) {
                    awayTeamInput.setError("Masukkan Nama Away Team");
                } else if (!siapUbahImgHome) {
                    Toast.makeText(PilihTim.this, "Gambar " + hometeam + " harus diset", Toast.LENGTH_SHORT).show();
                } else if (!siapUbahImgAway) {
                    Toast.makeText(PilihTim.this, "Gambar " + awayteam + " harus diset", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(PilihTim.this, Match.class);
                    intent.putExtra("namahome", hometeam);
                    intent.putExtra("namaaway", awayteam);
                    intent.putExtra("homeImg", homeImg.toString());
                    intent.putExtra("awayImg", awayImg.toString());
                    startActivity(intent);
                }
            }
        });

        homeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), HOME_REQUEST_CODE);
            }
        });

        awayLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), AWAY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            Log.d(TAG, "Pilih gambar dicancel");
            return;
        }
        else if(requestCode == HOME_REQUEST_CODE){
            if(data != null){
                try {
                    Uri imageUri = data.getData();
                    homeImg = imageUri;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    homeLogo.setImageBitmap(bitmap);
                    siapUbahImgHome = true;
                }
                catch (IOException error){
                    Toast.makeText(this, "Can't load image", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, error.getMessage());
                }
            }
        }
        else if(requestCode == AWAY_REQUEST_CODE){
            if(data != null){
                try {
                    Uri imageUri = data.getData();
                    awayImg = imageUri;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    awayLogo.setImageBitmap(bitmap);
                    siapUbahImgAway = true;
                }
                catch (IOException error){
                    Toast.makeText(this, "Can't load image", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, error.getMessage());
                }
            }
        }
    }
}
