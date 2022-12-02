package edu.usc.eventme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;


import androidx.databinding.DataBindingUtil;

import edu.usc.eventme.databinding.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

public class MapsFragment extends Fragment {


    private static final String TAG = MapsFragment.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition cameraPosition;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    // [START maps_current_place_state_keys]
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    // [END maps_current_place_state_keys]

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;

    private FusedLocationProviderClient fusedLocationClient;
    private FusedLocationProviderClient client;

    private double longitude=0.0, latitude=0.0;
    private Location currentlocation;
    HashMap<String, String> markermap = new HashMap<String, String>();
    ArrayList<LatLng> mMarkerPoints;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;
    private ImageView backButton;
    //TextView tvDistanceDuration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view=inflater.inflate(R.layout.fragment_maps, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        mMarkerPoints = new ArrayList<LatLng>();

        //String adr2= results.getEventList().get(1).getLocation();

//        place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
//        place2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location 2");

                        // Async map

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap=googleMap;
                client = LocationServices .getFusedLocationProviderClient(getActivity());

                mMap.setMyLocationEnabled(true);

                // Setting onclick event listener for the map
//                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(LatLng point) {
//                        // Already two locations
//                        if(mMarkerPoints.size()>1){
//                            mMarkerPoints.clear();
//                            mMap.clear();
//                        }
//
//                        // Adding new item to the ArrayList
//                        mMarkerPoints.add(point);
//
//                        // Creating MarkerOptions
//                        MarkerOptions options = new MarkerOptions();
//
//                        // Setting the position of the marker
//                        options.position(point);
//
//                        /**
//                         * For the start location, the color of marker is GREEN and
//                         * for the end location, the color of marker is RED.
//                         */
//                        if(mMarkerPoints.size()==1){
//                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                        }else if(mMarkerPoints.size()==2){
//                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                        }
//
//                        // Add new marker to the Google Map Android API V2
//                        mMap.addMarker(options);
//
//                        // Checks, whether start and end locations are captured
//                        if(mMarkerPoints.size() >= 2){
//                            mOrigin = mMarkerPoints.get(0);
//                            mDestination = mMarkerPoints.get(1);
//                            drawRoute();
//                        }
//
//                    }
//                });
                boolean permission=false;
                    if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        getCurrentLocation();
                    } else {

                    }
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    EventList results = new EventList();
                    db.collection("events").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("succeed!!!!!\n");
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Event event = document.toObject(Event.class);
                                            results.addEvent(event);
                                            LatLng temploc = new LatLng(event.getLatitude(), event.getLongitude());
                                            mMarkerPoints.add(temploc);
                                            Marker marker = mMap.addMarker(new MarkerOptions().position(temploc));
                                            marker.setTitle(event.getEventTitle());
                                            markermap.put(marker.getId(), event.getId());
//                                            if(mMarkerPoints.size()==2){
//                                                LatLng origin = mMarkerPoints.get(0);
//                                                LatLng dest = mMarkerPoints.get(1);
//                                                mOrigin = mMarkerPoints.get(0);
//                                                mDestination = mMarkerPoints.get(1);
//                                                drawRoute();
//                                            }
                                            //Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
//                                                    .clickable(true)
//                                                    .add(
//                                                            new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude()),
//                                                            temploc));
                                        }
                                        //results.sort("price");
                                    }
                                    else {
                                        System.out.println("No Event"+ task.getException().getMessage());
                                    }

                                }
                            });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        mDestination=marker.getPosition();
                        drawRoute();
                        BottomsheetFragment bottomSheet = new BottomsheetFragment();
                        Bundle args = new Bundle();
                        args.putString("currentid", markermap.get(marker.getId()));
                        args.putDouble("lat", latitude);
                        args.putDouble("lon", longitude);
                        //System.out.println(markermap);
                        //System.out.println("Id:"+marker.getId()+", eventid:"+markermap.get(marker.getId()));
                        bottomSheet.setArguments(args);
                        bottomSheet.show(getActivity().getSupportFragmentManager(),bottomSheet.getTag());

                        return true;
                    }
                });
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When clicked on map
                        // Initialize marker options
                        MarkerOptions markerOptions=new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Set title of marker
                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                        // Remove all marker
                        //googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        // Add marker on map
                        //googleMap.addMarker(markerOptions);
                    }
                });
            }

            @SuppressLint("MissingPermission")
            private void getCurrentLocation()
            {
                // Initialize Location manager
                LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                // Check condition
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    // When location service is enabled
                    // Get last location
                    client.getLastLocation().addOnCompleteListener(
                            new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(
                                        @NonNull Task<Location> task)
                                {

                                    // Initialize location
                                    Location location = task.getResult();
                                    System.out.println("got location");
                                    // Check condition
                                    if (location != null) {
                                        // When location result is not
                                        // null set latitude
                                        System.out.println("set location");
                                        System.out.println(location.getLatitude());
                                        latitude=location.getLatitude();
                                        // set longitude
                                        longitude= location.getLongitude();
                                        currentlocation=location;
                                        System.out.println(currentlocation.getLatitude());
                                        LatLng currentlatlng=new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
                                        mMarkerPoints.add(currentlatlng);
                                        mOrigin =currentlatlng;
//                                        mMap.addMarker(new MarkerOptions().position(currentlatlng).title("Your position"));
                                        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(currentlatlng, 15);;
                                        mMap.moveCamera(cu);
                                        mMap.setMyLocationEnabled(true);

//                                        mMap.setOnMyLocationButtonClickListener(this);
//                                        mMap.setOnMyLocationClickListener(this);
                                    }
                                    else {
                                        // When location result is null
                                        // initialize location request
                                        LocationRequest locationRequest= new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).setFastestInterval(1000).setNumUpdates(1);

                                        // Initialize location call back
                                        LocationCallback locationCallback = new LocationCallback() {
                                            @Override
                                            public void
                                            onLocationResult(LocationResult locationResult)
                                            {
                                                // Initialize
                                                // location
                                                Location location1 = locationResult.getLastLocation();
                                                // Set latitude
                                                latitude=location1.getLatitude();
                                                // Set longitude
                                                longitude= location1.getLongitude();
                                                currentlocation=location1;
                                            }
                                        };

                                        // Request location updates
                                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                                    }
                                }
                            });
                }
                else {
                    // When location service is not enabled
                    // open location setting
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }

            }

            public LatLng getLocationFromAddress(Context context, String strAddress, boolean hasmarker, Event event) {
                Geocoder coder = new Geocoder(context);
                List<Address> address;
                LatLng p1 = null;
                int trytime = 0;
                while (trytime < 2) {
                    try {
                        // May throw an IOException
                        address = coder.getFromLocationName(strAddress, 5);
                        if (address == null) {
                            return null;
                        }

                        Address location = address.get(0);
                        p1 = new LatLng(location.getLatitude(), location.getLongitude());
                        trytime=3;
                        if(hasmarker) {
                            Marker marker = mMap.addMarker(new MarkerOptions().position(p1).title("1"));
                            System.out.println("event:"+event.getEventTitle());
                            System.out.println(location.getLatitude());
                            System.out.println(location.getLongitude());
                            markermap.put(marker.getId(), event.getId());
                            marker.showInfoWindow();
                        }
                    } catch (IOException ex) {

                        trytime++;
                    }
                    catch (Exception ex) {
                        trytime++;
                    }


                }
                return p1;
            }
        });
        // Return view
        return view;
    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=AIzaSyDcHkBuK_91cmw6jwgBvyaw5A_IGw5pj6s";


        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        System.out.println(url);
        return url;
    }

    private void drawRoute(){

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mOrigin, mDestination);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getActivity().getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }
    }
}



