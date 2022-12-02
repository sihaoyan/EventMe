package edu.usc.eventme;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.adapters.SearchViewBindingAdapter;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.gridlayout.widget.GridLayout;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.EventTarget;
import com.google.firebase.database.core.view.EventGenerator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SearchView searchView;
    private Toolbar toolbar;
    private GridLayout gridLayout;
    private ImageView music;
    private ImageView outdoor;
    private ImageView food;
    private ImageView art;
    private Button bt;
    private EditText start;
    private EditText end;
    private String startDate, endDate;
    private String mParam1;
    private String mParam2;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchByKeyword(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        music = view.findViewById(R.id.image_music);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mysearch("Music","category");
            }
        });
        outdoor = view.findViewById(R.id.image_outdoor);
        outdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mysearch("Outdoor","category");
            }
        });
        food = view.findViewById(R.id.image_food);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mysearch("Food","category");
            }
        });
        art = view.findViewById(R.id.image_art);
        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mysearch("Arts","category");

            }
        });


        start = view.findViewById(R.id.startDate);
        end = view.findViewById(R.id.endDate);
        bt = view.findViewById(R.id.button2);
        bt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                startDate=start.getText().toString();
                endDate=end.getText().toString();
                String regex = "^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(startDate);
                Matcher matcher2 = pattern.matcher(endDate);
                boolean isMatch = matcher.matches()&matcher2.matches();
                if(isMatch){
                    int check = startDate.compareTo(endDate) ;
                    if(check<0)
                        searchByDate(startDate, endDate);
                    else
                        showMessage("The start date should be earlier than the end date");
                }
                else{
                    showMessage("Please enter correct date format!");
                }

            }
        });
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        //activity.setSupportActionBar().setTitle("Custom Toolbar");
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                return;
            }

        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.explore_menu, menu);
//        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//        searchView.setIconified(true);
//        SearchManager searchM = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchM.getSearchableInfo(getActivity().getComponentName()));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                mysearch(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                mysearch(query)2
//                return true;
//            }
//        });
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    private void mysearch(String query, String searchBy) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Intent intent = new Intent(getActivity(), EventBoxes.class);
        String keyWord = query;
        String type = searchBy;
        EventList results = new EventList();
        Query search = db.collection("events").whereEqualTo(type,keyWord);
        search.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Event event = document.toObject(Event.class);
                                results.addEvent(event);
                            }
                            results.sort("cost");
                            intent.putExtra("searchResult", results);
                            startActivity(intent);
                        } else {
                            showMessage("No Event"+ task.getException().getMessage());
                        }

                    }
                });
    }

    //search with two inputs
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void searchByDate(String query, String query2) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Intent intent = new Intent(getActivity(), EventBoxes.class);
        EventList results = new EventList();
        Query search = db.collection("events");
        search.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Event event = document.toObject(Event.class);
                                results.addEvent(event);
                            }

                        } else {
                            showMessage("No Event"+ task.getException().getMessage());
                        }
                        EventList searchRe = new EventList();
                        for(Event e:results.getEventList()){
                            String startD=e.getStartDate();
                            String endD=e.getEndDate();
                            if(query.compareTo(endD)<=0 && startD.compareTo(query2)<=0){
                                searchRe.addEvent(e);
                            }
                        }
                        searchRe.sort("cost");
                        intent.putExtra("searchResult", searchRe);
                        startActivity(intent);
                    }
                });

    }

    private void searchByKeyword(String keyword){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Intent intent = new Intent(getActivity(), EventBoxes.class);
        EventList results = new EventList();
        Query search = db.collection("events");
        search.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Event event = document.toObject(Event.class);
                                results.addEvent(event);
                            }
                        } else {
                            showMessage("No Event"+ task.getException().getMessage());
                        }
                        //showMessage(String.valueOf(results.getEventList().size()));
                        EventList searchRe = new EventList();
                        for(Event e:results.getEventList()){
                            String name = e.getEventTitle();
                            String organize = e.getSponsoringOrganization();
                            String location = e.getLocation();
                            if(name.toLowerCase().contains(keyword.toLowerCase())|organize.toLowerCase().contains(keyword.toLowerCase())|location.toLowerCase().contains(keyword)){
                                searchRe.addEvent(e);
                            }
                        }
                        searchRe.sort("cost");
                        intent.putExtra("searchResult", searchRe);
                        startActivity(intent);
                    }
                });

    }


//    private void createEvents(){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String eventTitle = "Home Coming Concert";
//        int numUser = 350;
//        String category = "Music";
//        String startDate = "09/01/2023";
//        String endDate = "09/01/2023";
//        String startTime = "17:00";
//        String endTime = "21:00";
//        String cost = "$";
//        Boolean parking = true;
//        String description = "USC will be holding this big concert for welcoming students back on campus. The guest singers include Justin Bieber, Post Malone, and Drake.";
//        String sponsoringOrganization = "USC Activity";
//        List<String> users = new ArrayList<String>();
//        String photoURL = "abcccc";
//        for(int i=0;i<=20;i++) {
//            Event event = new Event.Builder(String.valueOf(i), eventTitle, category, endDate, startDate, endTime, startTime, numUser, description, cost, parking, sponsoringOrganization, users, photoURL).build();
//            db.collection("events").document(String.valueOf(i))
//                    .set(event);
//        }
//        showMessage("Events created!");
//    }




    //show message
    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

}

