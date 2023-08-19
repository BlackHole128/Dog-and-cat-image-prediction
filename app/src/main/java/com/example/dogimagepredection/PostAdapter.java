package com.example.dogimagepredection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {

    ArrayList<Postmodel> posts;
    Context context;

    public PostAdapter(ArrayList<Postmodel> posts, Context context) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_items, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Postmodel model = posts.get(position);
        final Boolean[] liked = {false};

        Picasso.get()
                .load(model.getPostImage())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.postImage);


        FirebaseFirestore.getInstance().collection("User")
                .document(model.getPostedby()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        Picasso.get()
                                .load(user.getUserImage())
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(holder.profileImage);

                        holder.name.setText(user.getName());
                    }
                });


        long timestamp = model.getPostedAt(); // Replace this with your timestamp value in milliseconds
        Date date = new Date(timestamp);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()); // Replace the format string as per your needs
        String formattedDate = sdf.format(date);

        holder.date.setText(formattedDate);
        holder.likes.setText(model.getPostLikes() + "");
        holder.comments.setText(model.getComments() + "");

        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(model.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            liked[0] = true;
                            holder.likes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_fill, 0, 0, 0);
                        } else {
                            liked[0] = false;
                            holder.likes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_blank, 0, 0, 0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!liked[0]) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("posts")
                            .child(model.getPostId())
                            .child("likes")
                            .child(FirebaseAuth.getInstance().getUid())
                            .setValue(true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(model.getPostId())
                                            .child("postLikes")
                                            .setValue(model.getPostLikes() + 1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    liked[0] = true;
                                                    holder.likes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_fill, 0, 0, 0);

                                                }
                                            });
                                }
                            });
                } else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("posts")
                            .child(model.getPostId())
                            .child("likes")
                            .child(FirebaseAuth.getInstance().getUid())
                            .removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(model.getPostId())
                                            .child("postLikes")
                                            .setValue(model.getPostLikes() - 1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    liked[0] = false;
                                                    holder.likes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_blank, 0, 0, 0);

                                                }
                                            });
                                }
                            });
                }
            }
        });
    }


//        holder.comments.setOnClickListener(v ->
//
//            {
//                Intent intent = new Intent(context, CommentActivity.class);
//                intent.putExtra("postId", model.getPostId());
//                intent.putExtra("postedBy", model.getPostedBy());
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            });
//        }

        @Override
        public int getItemCount () {
            return posts.size();
        }

        public static class viewHolder extends RecyclerView.ViewHolder {

            ImageView postImage;
            CircleImageView profileImage;
            TextView name, date;
            TextView likes, comments;

            public viewHolder(@NonNull View itemView) {
                super(itemView);
                postImage = itemView.findViewById(R.id.postImage);
                profileImage = itemView.findViewById(R.id.profileImage);
                name = itemView.findViewById(R.id.name);
                date = itemView.findViewById(R.id.date);
                likes = itemView.findViewById(R.id.likes);
                comments = itemView.findViewById(R.id.comments);
            }
        }
    }