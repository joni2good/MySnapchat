package com.example.mysnapchat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.TextView;

import com.example.mysnapchat.adapter.Adapter;
import com.example.mysnapchat.models.MyImage;
import com.example.mysnapchat.repos.Repo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Updateable{

    List<MyImage> images = new ArrayList();
    static String username = "Default username";

    ListView listView;
    Adapter adapter;
    Button sendButton;
    Button setUsername;
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

        adapter = new Adapter(images, this);
        listView.setAdapter(adapter);

        Repo.r().setup(this, images);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyImage tempImg = images.get(position);

                Intent imageIntent = new Intent(MainActivity.this, ImageActivity.class);
                imageIntent.putExtra("text", tempImg.getText());
                imageIntent.putExtra("id", tempImg.getId());
                imageIntent.putExtra("user", tempImg.getUser());
//                System.out.println(tempImg.getId() + " " + tempImg.getUser() + " " + tempImg.getText());
                startActivity(imageIntent);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        setUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textUsername.getText().toString().equals("")){
                    username = "all";
                }else {
                    username = textUsername.getText().toString();
                }
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, 1);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
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
            editImageText.setText("");
        }
    }

    public void update(Object o) {
        adapter.notifyDataSetChanged();
    }
}