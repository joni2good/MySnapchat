package com.example.mysnapchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysnapchat.adapter.Adapter;
import com.example.mysnapchat.models.MyImage;
import com.example.mysnapchat.repos.MapRepo;
import com.example.mysnapchat.repos.Repo;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Updateable {

    List<MyImage> images = new ArrayList();
    static String username = "Default username";
    Adapter adapter;

    ListView listView;
    Button sendButton;
    Button setUsername;
    Button openMap;
    TextView editImageText;
    TextView textUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        sendButton = findViewById(R.id.sendButton);
        editImageText = findViewById(R.id.editImageText);
        setUsername = findViewById(R.id.setUsername);
        textUsername = findViewById(R.id.textUsername);
        openMap = findViewById(R.id.mapButton);

        adapter = new Adapter(images, this);
        listView.setAdapter(adapter);

        Repo.r().setup(this, images);

        startListListener();
        getPermission();
    }

    public void startListListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyImage tempImg = images.get(position);

                Intent imageIntent = new Intent(MainActivity.this, ImageActivity.class);
                imageIntent.putExtra("text", tempImg.getText());
                imageIntent.putExtra("id", tempImg.getId());
                imageIntent.putExtra("user", tempImg.getUser());
                startActivity(imageIntent);
            }
        });
    }

    public void onUsernameClick(View v) {
        if (textUsername.getText().toString().equals("")) {
            username = "all";
        } else {
            username = textUsername.getText().toString();
        }
    }

    public void getPermission(){
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startMapIntent(View view) {
        Intent mapIntent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(mapIntent);
    }

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, 1);
        } catch (ActivityNotFoundException e) {
            System.out.println("Can't take picture, activity not found: " + e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = Repo.r().drawTextToBitmap(imageBitmap, editImageText.getText().toString());
            Repo.r().addImage(new MyImage(username, imageBitmap, editImageText.getText().toString()));

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            try {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                System.out.println(location.getLatitude() + " " + location.getLongitude());
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MapRepo.r().addPin(username, latLng);
            }catch (Exception e){
                Toast.makeText(this, "Can't update location, permission denied", Toast.LENGTH_LONG).show();
            }

            editImageText.setText("");
        }
    }

    public void update(Object o) {
        adapter.notifyDataSetChanged();
    }
}