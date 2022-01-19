package com.example.whereabuses;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MessageHolder extends RecyclerView.ViewHolder {
    TextView mText;
    TextView mUsername;
    TextView mTime;
    TextView mLikesCount;
    ImageView imgProfile, imgDropdown, imgLikes;
    public MessageHolder(View itemView) {
        super(itemView);

    }
}
