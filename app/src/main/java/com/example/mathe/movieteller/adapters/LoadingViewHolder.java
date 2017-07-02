package com.example.mathe.movieteller.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mathe.movieteller.R;

class LoadingViewHolder extends RecyclerView.ViewHolder {
    ProgressBar progressBar;

    LoadingViewHolder(View view) {
        super(view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
    }
}