package com.example.mysnapchat.repos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.mysnapchat.MainActivity;
import com.example.mysnapchat.TaskListener;
import com.example.mysnapchat.Updateable;
import com.example.mysnapchat.models.MyImage;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repo {
    private static Repo repo = new Repo();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private final String COLLECTION = "images";
    public List<MyImage> images = new ArrayList();
    private Updateable activity;

    public static Repo r(){
        return repo;
    }

    public void setup(Updateable a, List<MyImage> list){
        activity = a;
        images = list;
        startListener();

    }

    public void addImage(MyImage image){
        DocumentReference reference = db.collection(COLLECTION).document();
        Map<String, String> map = new HashMap();
        map.put("user", image.getUser());
        map.put("text", image.getText());
        reference.set(map); // replaces previous values
        image.setId(reference.getId());
        uploadBitmap(image);
        System.out.println("Inserted " + reference.getId());
    }

    public List<MyImage> getImages() {
        return images;
    }

    // This is an important method because it connects the view with our data, we choose what data is relevant and collect that so that other methods can access the correct parts of our IMG storage
    // Without this method the view isn't updated and the view can't reference image id and therefor cant use other methods
    // The method waits for a change to happen and then it gets the necessary data when it happens
    public void startListener(){
        db.collection(COLLECTION).addSnapshotListener((values, error) -> {
            images.clear();
            if (error == null) {
                for (DocumentSnapshot snap : values.getDocuments()) {
                    Object user = snap.get("user");
                    Object text = snap.get("text");
                    if (user != null) {
                        images.add(new MyImage(snap.getId(), user.toString(), text.toString()));
                    } else {
                        System.out.println("Error getting documents");
                    }
                }
            }
            activity.update(null);
        });
    }

    public void uploadBitmap(MyImage image){
        StorageReference ref = storage.getReference(image.getId());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.getImageBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos);

        ref.putBytes(baos.toByteArray()).addOnCompleteListener(snap -> {
            System.out.println("Ok on upload: " + snap);
        }).addOnFailureListener(exception -> {
            System.out.println("Fail on upload:" + exception);
        });
    }

    // This method is important because without it we can't display an image
    // The method relies on startListener, but is equally important because we need the image.
    // The method gets the bytes needed to create the image and then calls a method in ImageActivity that converts it to the necessary data
    public void downloadBitmap(String id, TaskListener taskListener){
        StorageReference reference = storage.getReference(id);
        int max = 1024 * 1024;
        reference.getBytes(max).addOnSuccessListener(bytes -> {
            taskListener.receive(bytes);
        }).addOnFailureListener(exception -> {
            System.out.println("Failure receiving image: " + exception);
        });
    }

    public void deleteImage(MyImage image){
        DocumentReference documentReference = db.collection(COLLECTION).document(image.getId());
        documentReference.delete();
        StorageReference storageReference = storage.getReference(image.getId());
        storageReference.delete();
    }

    public Bitmap drawTextToBitmap(Bitmap image, String gText) {
        Bitmap.Config bitmapConfig = image.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        image = image.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// new antialised Paint
        paint.setColor(Color.rgb(161, 161, 161));
        paint.setTextSize((int) (20)); // text size in pixels
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE); // text shadow
        canvas.drawText(gText, 10, 100, paint);
        return image;
    }
}
