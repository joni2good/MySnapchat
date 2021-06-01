package com.example.mysnapchat.repos;

import com.example.mysnapchat.Updateable;
import com.example.mysnapchat.models.Pin;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapRepo {
    private static MapRepo repo = new MapRepo();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String COLLECTION = "pins";
    public List<Pin> pins = new ArrayList();
    private Updateable activity;

    public static MapRepo r(){
        return repo;
    }

    public void setup(Updateable a, List<Pin> list){
        activity = a;
        pins = list;
        startListener();
    }

    public void addPin(String name, LatLng latLng){
        DocumentReference reference = db.collection(COLLECTION).document();
        Map<String, Object> map = new HashMap();
        map.put("name", name);
        map.put("latLng", new GeoPoint(latLng.latitude, latLng.longitude));
        reference.set(map); // replaces previous values unless SetOption.merge()
        System.out.println("Inserted " + reference.getId());
    }

    public void updatePin(Pin pin){
        DocumentReference reference = db.collection(COLLECTION).document(pin.getId());
        Map<String, Object> map = new HashMap();
        map.put("name", pin.getName());
        map.put("latLng", new GeoPoint(pin.getLatLng().latitude, pin.getLatLng().longitude));
        reference.update(map); // update previous values
        System.out.println("Updated " + reference.getId());
    }

//    public Pin checkPinExists(String name) {
//        Pin tempPin = new Pin();
//        List<Pin> pinList = getPins();
//        for (Pin pin : pinList) {
//            System.out.println(pin.getName() + " and name " + name);
//            if (pin.getName().equals(name)) {
//                System.out.println("Pin name and id" + pin.getName() + pin.getId());
//                return pin;
//            }
//        }
//        tempPin.setId("CreateNew");
//        return tempPin;
//    }
//
//    public synchronized List<Pin> getPins() {
//        List<Pin> returnPins = new ArrayList();
//        db.collection(COLLECTION).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                    GeoPoint pos = qds.getGeoPoint("latLng");
//                    returnPins.add(new Pin(qds.getId(), qds.get("name").toString(), new LatLng(pos.getLatitude(), pos.getLongitude())));
//                }
//            }
//        });
//        return returnPins;
//    }

    public void startListener(){
        db.collection(COLLECTION).addSnapshotListener((values, error) -> {
            pins.clear();
            if (error == null) {
                for (DocumentSnapshot snap : values.getDocuments()) {
                    Object name = snap.get("name");
                    GeoPoint pos = snap.getGeoPoint("latLng");
                    if (name != null) {
                        LatLng latLng = new LatLng(pos.getLatitude(), pos.getLongitude());
                        pins.add(new Pin(snap.getId(), name.toString(), latLng));
                    } else {
                        System.out.println("Error getting documents");
                    }
                }
            }
            activity.update(null);
        });
    }

    public void deletePin(Pin pin){
        DocumentReference documentReference = db.collection(COLLECTION).document(pin.getId());
        documentReference.delete();
    }
}
