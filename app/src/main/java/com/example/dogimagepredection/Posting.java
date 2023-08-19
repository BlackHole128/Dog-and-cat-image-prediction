package com.example.dogimagepredection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogimagepredection.databinding.ActivityPostingBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

public class Posting extends AppCompatActivity {
    ActivityPostingBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    Uri image_uri;
    ProgressDialog mLoadingBar;
    ImageView imageView;
    Button button;

    RecyclerView recyclerView;
    ArrayList<Postmodel> posts;

    static final int GALLERY_IMAGE_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = findViewById(R.id.addImagePost_Id);
        button = findViewById(R.id.send_button);
        recyclerView = findViewById(R.id.posts);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        mLoadingBar = new ProgressDialog(this);
        posts = new ArrayList<>();

        getDataFromDatabase();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_IMAGE_CODE);
            }

        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uplodePost();
            }
        });


    }

    private void getDataFromDatabase() {
        PostAdapter postAdapter = new PostAdapter(posts, getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(postAdapter);

        firebaseDatabase.getReference().child("posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        posts.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Postmodel post = dataSnapshot.getValue(Postmodel.class);
                            post.setPostId(dataSnapshot.getKey());
                            posts.add(post);
                        }
                        postAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void uplodePost() {
        mLoadingBar.setTitle("uploding");
        mLoadingBar.show();

        final String timeStamp = String.valueOf(System.currentTimeMillis());
        final StorageReference reference = storage.getReference().child("post_photo").child(firebaseAuth.getUid()).child(timeStamp);

        reference.putFile(image_uri).addOnSuccessListener(taskSnapshot -> {
//                Toasty.success(getApplicationContext(), "Image saved", Toast.LENGTH_SHORT, true).show();
            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Postmodel post = new Postmodel();
                    post.setPostImage(uri.toString());
                    post.setPostedby(FirebaseAuth.getInstance().getUid());
                    post.setPostedAt(new Date().getTime());

                    firebaseDatabase.getReference().child("posts").push().setValue(post)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    mLoadingBar.dismiss();
                                    Toast.makeText(Posting.this, "Post uploaded!", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(getApplicationContext(), BlogActivity.class));
//                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mLoadingBar.dismiss();
                                    Toast.makeText(Posting.this, "Post uploaded!", Toast.LENGTH_SHORT).show();
                                }
                            });

//                    imageView.setImageURI(null);

                }
            });
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_IMAGE_CODE) {
                if (data != null) {
                    image_uri = data.getData();
                    imageView.setImageURI(image_uri);
                    button.setEnabled(true);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}