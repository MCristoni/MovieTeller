package com.example.mathe.movieteller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mathe.movieteller.R;
import com.example.mathe.movieteller.classes.ResultsTrailer;
import com.example.mathe.movieteller.classes.Videos;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class RecyclerAdapter extends ExpandableRecyclerViewAdapter<TitleViewHolder, SubItemViewHolder> {

    private Context context;
    public RecyclerAdapter(Context context, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.context = context;
    }

    @Override
    public TitleViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videos_item, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public SubItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videos_subitem, parent, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(SubItemViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {

        final ResultsTrailer subTitle = ((Videos) group).getItems().get(childIndex);
        holder.setSubTitletName(subTitle.getName());
        if (((Videos)group).getItems().get(childIndex).getSite().equalsIgnoreCase("youtube"))
        {
            holder.setImgVideo(R.drawable.youtube_play);
            holder.setYoutube(true);
            holder.setVideoKey(((Videos)group).getItems().get(childIndex).getKey());
        }
        else {
            holder.setImgVideo(R.drawable.video_icon);
            holder.setYoutube(false);
        }
    }

    @Override
    public void onBindGroupViewHolder(TitleViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGenreTitle(group);
    }
}