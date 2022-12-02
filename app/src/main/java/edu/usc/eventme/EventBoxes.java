package edu.usc.eventme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class EventBoxes extends AppCompatActivity {
    private RecyclerView ry;
    //private Button back;
    private ImageView back;
    String[] sortBy;
    AutoCompleteTextView autoView;
    private FusedLocationProviderClient client;
    private boolean finishlocation=false;
    private double latitude=-1.0;
    private double longitude=-1.0;
    private FusedLocationProviderClient fusedLocationClient;
    Context currentcontext;


    @SuppressLint("MissingPermission")
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

                                setContentView(R.layout.eventboxses_layout);

                                EventList results = (EventList) getIntent().getSerializableExtra("searchResult");
                                results.setCurrentlat(latitude);
                                results.setCurrentlon(longitude);

                                ry = findViewById(R.id.recyclerView);
                                back = findViewById(R.id.backButton);
                                Intent toMain = new Intent(currentcontext, MainActivity.class);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(toMain);
                                    }
                                });

                                sortBy = getResources().getStringArray(R.array.sortBy);
                                autoView = findViewById(R.id.autoCompleteTextView);
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, sortBy);
                                autoView.setAdapter(arrayAdapter);
                                autoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent refresh = new Intent(getApplication(), EventBoxes.class);
                                        results.sort(sortBy[i]);
                                        refresh.putExtra("searchResult", results);
                                        startActivity(refresh);
                                        finish();
                                    }
                                });

                                MyAdaptor myAdaptor = new MyAdaptor(currentcontext, results);
                                ry.setAdapter(myAdaptor);
                                LinearLayoutManager llm = new LinearLayoutManager(currentcontext);
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                ry.setLayoutManager(llm);

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

            setContentView(R.layout.eventboxses_layout);

            EventList results = (EventList) getIntent().getSerializableExtra("searchResult");
            results.setCurrentlat(latitude);
            results.setCurrentlon(longitude);

            ry = findViewById(R.id.recyclerView);
            back = findViewById(R.id.backButton);
            Intent toMain = new Intent( this,MainActivity.class);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(toMain);
                }
            });

            sortBy = getResources().getStringArray(R.array.sortBy);
            autoView = findViewById(R.id.autoCompleteTextView);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.dropdown_item,sortBy);
            autoView.setAdapter(arrayAdapter);
            autoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent refresh = new Intent(getApplication(),EventBoxes.class);
                    results.sort(sortBy[i]);
                    refresh.putExtra("searchResult",results);
                    startActivity(refresh);
                    finish();
                }
            });

            MyAdaptor myAdaptor = new MyAdaptor(this, results);
            ry.setAdapter(myAdaptor);
            LinearLayoutManager llm = new LinearLayoutManager(currentcontext);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            ry.setLayoutManager(llm);


        }



    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation()
    {
        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager)this
                .getSystemService(
                        Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            System.out.println("havepermission");
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            System.out.println("succeed");
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                latitude=location.getLatitude();
                                System.out.println("latitude");
                                longitude=location.getLongitude();// Logic to handle location object
                            }
                            else{
                                latitude=0.0;
                                longitude=0.0;
                            }
                        }
                    });
            System.out.println("setonclicker");
        }
        else {
            // When location service is not enabled
            // open location setting
            startActivity(
                    new Intent(
                            Settings
                                    .ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK));
            latitude=0.0;
            longitude=0.0;
        }

    }

}
