package com.example.dogimagepredection;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Rating extends AppCompatActivity {
    private RatingBar rateRatingBar;
    private EditText commentEditText;
    private TextView nameU;

    private ImageView buttonImageView;
    private ListView listView;
    private List<UserComment> userCommentList;
    private CommentCustomAdapter commentCustomAdapter;
    DatabaseReference databaseReference;

    String name;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        databaseReference = FirebaseDatabase.getInstance().getReference("RatingUser");
        rateRatingBar= findViewById(R.id.rateRatingBar_Id);
        commentEditText= findViewById(R.id.commentEditText_Id);
        nameU = findViewById(R.id.nameU);
        buttonImageView= findViewById(R.id.buttonImageView_Id);
        listView = findViewById(R.id.listView_Id);



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // do something with the user data
                        name = (String) documentSnapshot.get("name");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // handle the error
                    }
                });







        userCommentList = new ArrayList<>();
        commentCustomAdapter = new CommentCustomAdapter(Rating.this,userCommentList);
        //loadData();
        buttonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }


        });

    }

//    public void loadData() {
//
//
//    }

    public void saveData() {
        String comment = commentEditText.getText().toString().trim();
        String rating = String.valueOf(rateRatingBar.getRating());
        String name1 = name;
        if (TextUtils.isEmpty(comment)) {
            // if the text fields are empty
            // then show the below message.
            Toast.makeText(Rating.this, "Please add some data.", Toast.LENGTH_SHORT).show();
        }
        else {
            String key = databaseReference.push().getKey();
            UserComment userComment = new UserComment(comment,rating,name1);
            databaseReference.child(key).setValue(userComment);
            Toast.makeText(Rating.this, "Comment added", Toast.LENGTH_SHORT).show();
            commentEditText.setText("");
            float v = 0;
            rateRatingBar.setRating(v);
        }

    }

    @Override
    protected void onStart() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCommentList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren() )
                {
                    UserComment userComment = dataSnapshot1.getValue(UserComment.class);
                    userCommentList.add(userComment);

                }
                listView.setAdapter(commentCustomAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onStart();
    }

}