package com.example.mathe.movieteller.adapters;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mathe.movieteller.R;
import com.example.mathe.movieteller.classes.Videos;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class TitleViewHolder extends GroupViewHolder {

    private TextView titleName;
    private ImageView arrow;

    TitleViewHolder(View itemView) {
        super(itemView);
        titleName = (TextView) itemView.findViewById(R.id.txtTituloGrupo);
        arrow = (ImageView) itemView.findViewById(R.id.arrow);
    }

    void setGenreTitle(ExpandableGroup videos) {
        if (videos instanceof Videos) {
            titleName.setText(videos.getTitle());
        }
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}