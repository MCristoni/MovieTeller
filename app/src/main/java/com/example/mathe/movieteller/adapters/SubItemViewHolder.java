package com.example.mathe.movieteller.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathe.movieteller.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

class SubItemViewHolder extends ChildViewHolder {

    private TextView subTitleTextView;
    private ImageView imgVideo;
    private boolean youtube;
    private String videoKey;

    SubItemViewHolder(View itemView) {
        super(itemView);
        subTitleTextView = (TextView) itemView.findViewById(R.id.txtNomeVideo);
        imgVideo = (ImageView) itemView.findViewById(R.id.imgVideo);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (youtube) {
                    Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + videoKey);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    v.getContext().startActivity(intent);
                }
                else
                    Toast.makeText(v.getContext(), "Couldn't open this video!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setSubTitletName(String name) {
        subTitleTextView.setText(name);
    }

    void setImgVideo(int icon)
    {
        imgVideo.setImageResource(icon);
    }
    void setYoutube(boolean youtube) {
        this.youtube = youtube;
    }

    void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }
}