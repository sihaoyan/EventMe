package edu.usc.eventme;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private static final String TAG = "profile fragment: ";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ImageView userPhoto, eventImage;
    private TextView userName, userEmail, userBirthday;
    private Button logOutBtn, showEventButton;
    private FirebaseFirestore db;
    private User user;
    private ProgressBar progressBar;
    private TextView eventName, eventOrg, eventCost, eventLoc;
    private TextView eventDate, eventTime, emptyListMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                return;
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentUser != null) {
            getUser();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        userName = profileView.findViewById(R.id.userName);
        userBirthday = profileView.findViewById(R.id.userBirthday);
        userEmail = profileView.findViewById(R.id.userEmail);
        userPhoto = profileView.findViewById(R.id.userPhoto);
        logOutBtn = profileView.findViewById(R.id.logoutButton);
        showEventButton = profileView.findViewById(R.id.showEventButton);
        progressBar = profileView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        eventName = profileView.findViewById(R.id.eventName);
        eventImage = profileView.findViewById(R.id.eventImage);
        eventOrg = profileView.findViewById(R.id.eventOrg);
        eventCost = profileView.findViewById(R.id.eventCost);
        eventLoc = profileView.findViewById(R.id.eventLoc);
        eventDate = profileView.findViewById(R.id.eventLocation);
        eventTime = profileView.findViewById(R.id.eventTime);
        emptyListMessage = profileView.findViewById(R.id.emptyListMessage);
        emptyListMessage.setVisibility(View.GONE);

        if (currentUser != null) {
            progressBar.setVisibility(View.VISIBLE);
            logOutBtn.setEnabled(false);
            showEventButton.setEnabled(false);
            getUser();
        } else {
            userPhoto.getLayoutParams().width = 1;
            userPhoto.setVisibility(View.INVISIBLE);
            profileView.findViewById(R.id.eventView).getLayoutParams().height = 0;
            profileView.findViewById(R.id.breaker2).getLayoutParams().height = 0;
            userName.setText("Log in\nRegister events you like");
            userName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            userBirthday.setText("");
            userEmail.setText("");
            logOutBtn.setText("Log In");
            logOutBtn.setBackgroundColor(getActivity().getResources().getColor(R.color.orange_theme, getContext().getTheme()));
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mainActivity = new Intent(getContext(), LoginActivity.class);
                    startActivity(mainActivity);
                    mAuth.signOut();
                    finishFragment();
                }
            });
        }
//        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() != null) {
//                    progressBar.setVisibility(View.VISIBLE);
//                    DocumentReference docRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
//                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
//                                    user = document.toObject(User.class);
//                                    progressBar.setVisibility(View.GONE);
//                                    userBirthday.setText(user.getUserBirthday());
//                                    userName.setText(user.getUserName());
//                                    userEmail.setText(user.getUserEmail());
//                                    Picasso.get().load(user.getUserPhoto()).into(userPhoto);
//                                    logOutBtn.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            Intent mainActivity = new Intent(getContext(), LoginActivity.class);
//                                            mAuth.signOut();
//                                            startActivity(mainActivity);
//                                        }
//                                    });
//                                    showEventButton.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            Intent mainActivity = new Intent(getContext(), EventRegisterActivity.class);
//                                            startActivity(mainActivity);
//                                        }
//                                    });
//                                } else {
//                                    Log.i("Activity", "not ready");
//                                }
//                            } else {
//                                showMessage(task.getException().getMessage());
//                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            showMessage(e.getMessage());
//                        }
//                    });
//
//                } else {
//                    userPhoto.getLayoutParams().width = 1;
//                    userPhoto.setVisibility(View.INVISIBLE);
//                    profileView.findViewById(R.id.eventView).getLayoutParams().height = 0;
//                    profileView.findViewById(R.id.breaker2).getLayoutParams().height = 0;
//                    userName.setText("Log in\nRegister events you like");
//                    userName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                    userBirthday.setText("");
//                    userEmail.setText("");
//                    logOutBtn.setText("Log In");
//                    logOutBtn.setBackgroundColor(getActivity().getResources().getColor(R.color.orange_theme, getContext().getTheme()));
//                    logOutBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent mainActivity = new Intent(getContext(), LoginActivity.class);
//                            startActivity(mainActivity);
//                        }
//                    });
//                }
//            }
//        });
        return profileView;
    }

    private void getUser() {
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
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
                    showMessage(task.getException().getMessage());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage(e.getMessage());
            }
        });
    }

    private void updateUI() {
        progressBar.setVisibility(View.GONE);
        userBirthday.setText(user.getUserBirthday());
        userName.setText(user.getUserName());
        userEmail.setText(user.getUserEmail());
        logOutBtn.setEnabled(true);
        showEventButton.setEnabled(true);
        Picasso.get().load(user.getUserPhoto()).into(userPhoto);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getContext(), LoginActivity.class);
                mAuth.signOut();
                startActivity(mainActivity);
            }
        });
        if (user.getUserEventList().getEventList().size() == 0) {
            emptyListMessage.setVisibility(View.VISIBLE);
            eventName.setVisibility(View.GONE);
            eventTime.setVisibility(View.GONE);
            eventCost.setVisibility(View.GONE);
            eventDate.setVisibility(View.GONE);
            eventImage.setVisibility(View.GONE);
            eventLoc.setVisibility(View.GONE);
            eventOrg.setVisibility(View.GONE);
            showEventButton.setVisibility(View.GONE);
        } else {
            Event event = user.getUserEventList().getEventList().get(0);
            emptyListMessage.setVisibility(View.GONE);
            eventName.setText(event.getEventTitle());
            eventOrg.setText(event.getSponsoringOrganization());
            eventLoc.setText(event.getLocation());
            eventDate.setText(event.getStartDate() + " to " + event.getEndDate());
            eventTime.setText(event.getStartTime()+" to "+event.getEndTime());
            eventCost.setText(event.getCost());
            Picasso.get().load(event.getPhotoURL()).into(eventImage);
        }
        showEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getContext(), UserEventList.class);
                mainActivity.putExtra("userList", user.getUserEventList());
                startActivity(mainActivity);
            }
        });

    }

    private void finishFragment() {
        getActivity().getFragmentManager().popBackStack();
    }


    //show message
    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }



}