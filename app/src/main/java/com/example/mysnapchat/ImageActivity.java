package com.example.mysnapchat;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysnapchat.models.MyImage;
import com.example.mysnapchat.repos.Repo;

public class ImageActivity extends AppCompatActivity implements TaskListener {
    ImageView imageView;
    MyImage myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();
        myImage = new MyImage(intent.getStringExtra("id"), intent.getStringExtra("user"), intent.getStringExtra("text"));
        Repo.r().downloadBitmap(myImage.getId(), this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Repo.r().deleteImage(myImage);
    }

    @Override
    public void receive(byte[] bytes) {
        //recive bytes
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }

    public void imageClicked(View view){
        finish();
    }

}
