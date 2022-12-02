package edu.usc.eventme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserEventList extends AppCompatActivity {
    private RecyclerView ry;
    private ImageView backButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private String userID;
    private User user;
    private DocumentReference docRef;
    private double latitude=-1.0;
    private double longitude=-1.0;
    private FusedLocationProviderClient fusedLocationClient;
    Context currentcontext;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentcontext=this;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission
                        .ACCESS_FINE_LOCATION)
                == PackageManager
                .PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission
                        .ACCESS_COARSE_LOCATION)
                == PackageManager
                .PERMISSION_GRANTED) {
            LocationManager locationManager
                    = (LocationManager)this
                    .getSystemService(
                            Context.LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(
                    LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER)) {

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                System.out.println("succeed");
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    System.out.println("latitude");
                                    longitude = location.getLongitude();// Logic to handle location object
                                } else {
                                    latitude = 0.0;
                                    longitude = 0.0;
                                }

                                setContentView(R.layout.activity_user_event_list);
                                mAuth = FirebaseAuth.getInstance();
                                currentUser = mAuth.getCurrentUser();
                                db = FirebaseFirestore.getInstance();
                                userID = currentUser.getUid();
                                docRef = db.collection("users").document(userID);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                user = document.toObject(User.class);
                                                updateUI();
                                            } else {
                                                Log.i("Activity", "not ready");
                                            }
                                        } else {
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("Fail","did not get");
                                    }

                                });
                                //EventList userList = (EventList) getIntent().getSerializableExtra("userList");

                            }
                        });
            }
            else{
                startActivity(
                        new Intent(
                                Settings
                                        .ACTION_LOCATION_SOURCE_SETTINGS)
                                .setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
        else {
            latitude=0.0;
            longitude=0.0;

            setContentView(R.layout.activity_user_event_list);
            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            db = FirebaseFirestore.getInstance();
            userID = currentUser.getUid();
            docRef = db.collection("users").document(userID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            user = document.toObject(User.class);
                            updateUI();
                        } else {
                            Log.i("Activity", "not ready");
                        }
                    } else {
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Fail","did not get");
                }

            });
            //EventList userList = (EventList) getIntent().getSerializableExtra("userList");


        }
//        setContentView(R.layout.activity_user_event_list);
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//        db = FirebaseFirestore.getInstance();
//        userID = currentUser.getUid();
//        docRef = db.collection("users").document(userID);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        user = document.toObject(User.class);
//                        updateUI();
//                    } else {
//                        Log.i("Activity", "not ready");
//                    }
//                } else {
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.i("Fail","did not get");
//            }
//
//        });
        //EventList userList = (EventList) getIntent().getSerializableExtra("userList");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            updateUI();
        }
    }

    private void updateUI() {
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("Error", "Listen failed.", error);
                    return;
                }
                if (value != null && value.exists()) {
                    user = value.toObject(User.class);
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });
        EventList userList = user.getUserEventList();
        userList.setCurrentlat(latitude);
        userList.setCurrentlon(longitude);
        ry = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        MyAdaptor myAdaptor = new MyAdaptor(this, userList);
        ry.setAdapter(myAdaptor);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ry.setLayoutManager(llm);
    }
}