package edu.usc.eventme;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterActivity extends AppCompatActivity {

    ImageView ImgUserPhoto;
    Uri pickedImgUri;

    private EditText userEmail, userPassword, userConfirmPassword, userBirthday, userName;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar registerProgress;
    private Button registerButton;

    //get the uri of the chosen photo and place that on the profile photo
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    pickedImgUri = uri;
                    ImgUserPhoto.setImageURI(pickedImgUri);
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.editTextName);
        userEmail = findViewById(R.id.editTextTextEmailAddress);
        userBirthday = findViewById(R.id.editTextBirthday);
        userPassword = findViewById(R.id.editTextPW);
        userConfirmPassword = findViewById(R.id.editTextConfirmPW);
        registerProgress = findViewById(R.id.registerProgress);
        registerButton = findViewById(R.id.loginButton);
        registerProgress.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
        ImgUserPhoto = findViewById(R.id.regUserPhoto);
        //choose photo
        ImgUserPhoto.setOnClickListener((view)-> {
            mGetContent.launch("image/*");
        });
    }

    //when the register button is clicked, the registeration information is checked
    public void regClicked(View view) {
        final String email = userEmail.getText().toString();
        final String name = userName.getText().toString();
        final String birthday = userBirthday.getText().toString();
        final String password = userPassword.getText().toString();
        final String confirmPassword = userConfirmPassword.getText().toString();
        String regex = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(birthday);
        boolean isMatch = matcher.matches();

        if (pickedImgUri == null) {
            showMessage("Profile image cannot be empty.");
            return;
        } else if (email.isEmpty() || name.isEmpty() || birthday.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("There is some empty input.");
            return;
        } else if (!password.equals(confirmPassword)) {
            showMessage("Please check your password");
            return;
        } else if (!isMatch) {
            showMessage("Please enter a valid date");
            return;
        } else {
            registerProgress.setVisibility(View.VISIBLE);
            registerButton.setEnabled(false);
            CreateUserAccount(email,name,password,birthday,pickedImgUri);

        }
    }

    //show message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    //create user account on firebase, if success, update the information
    private void CreateUserAccount(String email, String name, String password, String birthday, Uri pickedImgUri) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUserInfo(name, birthday, pickedImgUri, mAuth.getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            showMessage("Authentication failed." + task.getException().getMessage());
                            registerProgress.setVisibility(View.GONE);
                            registerButton.setEnabled(true);
                        }
                    }
                });
    }

    //update user photo and name into the firebase storage
    private void updateUserInfo(String name, String birthday, Uri pickedImgUri, FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(currentUser.getUid()+".jpg");
        imageFilePath.putFile(pickedImgUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String userID = currentUser.getUid();
                        String TAG = "MyActivity";
                        User newUser = new User(name, currentUser.getEmail(), birthday, uri.toString());
                        //showMessage("test: " + userID);
                        DocumentReference newUserRef = db.collection("users").document(userID);
                        newUserRef.set(newUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        showMessage("Register complete");
                                        updateUI();
                                        //mAuth.signOut();
                                    }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                                registerProgress.setVisibility(View.GONE);
                                registerButton.setEnabled(true);

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        registerProgress.setVisibility(View.GONE);
                        registerButton.setEnabled(true);
                        showMessage(e.getMessage());
                    }
                });
            }
        });

    }

    private void updateUI() {
        registerProgress.setVisibility(View.GONE);
        registerButton.setEnabled(true);
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

}