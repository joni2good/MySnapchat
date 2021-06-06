package com.example.mysnapchat;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.example.mysnapchat.models.Pin;
import com.example.mysnapchat.repos.MapRepo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Updateable {

    private GoogleMap mMap;
    private List<Pin> pinList = new ArrayList();
    private Pin updatablePin = new Pin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // When ready calls onMapReady

        MapRepo.r().setup(this, pinList);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { // Needs google play to function, can only be called if installed
        mMap = googleMap;

        //LatLng 56.26392, 9.501785 equals Denmark
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(56.26392, 9.501785), 6.0f));

        mMap.setOnMapLongClickListener(latLng -> pinDialog(latLng));

        mMap.setOnMarkerClickListener(marker -> {
            System.out.println(marker.getTag());
            pinClickDialog(marker.getTag().toString());
            return false;
        });
    }

    private void pinDialog(LatLng latLng){
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("New pin")
                .setMessage("What is your pin name?")
                .setView(input)
                .setPositiveButton("OK", ((dialog, which) -> {
                    String name = input.getText().toString();
                    MapRepo.r().addPin(name, latLng);
                }))
                .setNegativeButton("Cancel", ((dialog, which) -> {
                    dialog.cancel();
                }));

        builder.show();
    }

    public void pinClickDialog(String id){
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        for (Pin pin1 : pinList){
            if (pin1.getId().equals(id)){
                updatablePin = pin1;
            }
        }

        input.setText(updatablePin.getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Edit pin")
                .setMessage("What is your pin name?")
                .setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updatablePin.setName(input.getText().toString());
                MapRepo.r().updatePin(updatablePin);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MapRepo.r().deletePin(updatablePin);
            }
        });

        builder.show();
    }

    @Override
    public void update(Object o) {
        System.out.println("Update called");
        Marker tempMarker;

        mMap.clear();
        for (Pin pin : pinList) {
            tempMarker = mMap.addMarker(new MarkerOptions().position(pin.getLatLng()).title(pin.getName()));
            tempMarker.setTag(pin.getId());
        }
    }
}