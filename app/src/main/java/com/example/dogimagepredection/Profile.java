package com.example.dogimagepredection;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dogimagepredection.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {
    ActivityProfileBinding binding;



   TextView textView;
   TextView date;
   TextView email;
   TextView number;

  ImageView imageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textView = findViewById(R.id.pro_name);
        date = findViewById(R.id.pro_birthday);
        email = findViewById(R.id.porgmail);
        number = findViewById(R.id.pro_number);
        imageView = findViewById(R.id.profile_image);





        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // do something with the user data
                      String  name1 = (String) documentSnapshot.get("name");
                      textView.setText(name1);

                      String email1 = (String) documentSnapshot.get("eamil");
                      email.setText(email1);

                      String date1 = (String) documentSnapshot.get("birthDay");
                      date.setText(date1);


                      String number1 = (String) documentSnapshot.get("number");
                      number.setText(number1);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // handle the error
                    }
                });




    }
}