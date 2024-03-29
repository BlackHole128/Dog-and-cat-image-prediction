package com.example.dogimagepredection;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class CommentCustomAdapter extends ArrayAdapter<UserComment> {
    private Activity context;
    private List<UserComment> userCommentList;

    public CommentCustomAdapter( Activity context, List<UserComment> userCommentList) {
        super(context, R.layout.comment_sample_layout, userCommentList);
        this.context = context;
        this.userCommentList = userCommentList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view= layoutInflater.inflate(R.layout.comment_sample_layout,null,true);

        UserComment userComment = userCommentList.get(position);

        TextView commentTextView= view.findViewById(R.id.commentTextView_Id);
        TextView nameTextVuew = view.findViewById(R.id.nameTextView_Id);
        TextView rateTextView=view.findViewById(R.id.rateTextView_Id);


        commentTextView.setText("Comment :"+userComment.getComment());
        nameTextVuew.setText("User :"+userComment.getName());
        rateTextView.setText("Rating :"+userComment.getRating());
        return view;
    }
}
