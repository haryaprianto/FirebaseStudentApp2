package com.example.a16022596.firebasestudentapp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    private UserProfile user;

    private EditText etName, etContactNo,etHobbies,etEmail;
    private Button btnUpdate;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userListRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        etName = (EditText)findViewById(R.id.editTextName);
        etContactNo = (EditText)findViewById(R.id.editTextContactNo);
        etEmail = (EditText)findViewById(R.id.editTextEmail);
        etHobbies = (EditText)findViewById(R.id.editTextHobbies);

        btnUpdate = (Button)findViewById(R.id.buttonUpdate);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        userListRef = firebaseDatabase.getReference("profiles/"+ firebaseUser.getUid());

        userListRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("EditProfileActivity", "onChildAdded()");
                UserProfile profile = dataSnapshot.getValue(UserProfile.class);
                if (profile != null) {
                    etName.setText(profile.getName());
                    etHobbies.setText(profile.getHobbies());
                    etContactNo.setText(profile.getContactNo());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.i("EditProfileActivity", "onChildChanged()");

            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("EditProfileActivity", "onChildMoved()");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("EditProfileActivity", "Database error occurred", databaseError.toException());
            }
        });

        Intent intent = getIntent();
        user = (UserProfile) intent.getSerializableExtra("UserProfile");
//        etName.setText(user.getName());
//        etHobbies.setText(user.getHobbies());
//        etContactNo.setText(user.getContactNo());
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Update Student record based on input given
                String newName = etName.getText().toString();
                String newHobbies = etHobbies.getText().toString();
                String newContactNo = etContactNo.getText().toString();
                UserProfile update = new UserProfile(newName, newContactNo,newHobbies);
                userListRef.child(firebaseUser.getUid()).setValue(update);
                Toast.makeText(getApplicationContext(), "User record updated successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });




    }
}
