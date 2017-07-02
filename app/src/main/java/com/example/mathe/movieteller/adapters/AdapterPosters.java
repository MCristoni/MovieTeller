package com.example.mathe.movieteller.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.mathe.movieteller.R;
import com.example.mathe.movieteller.classes.Results;
import com.example.mathe.movieteller.listeners.OnLoadMoreListener;
import com.example.mathe.movieteller.utils.Aplicacao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AdapterPosters extends RecyclerView.Adapter
{
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private List<Results> itens;
    private int lastVisibleItem;
    private int totalItemCount;
    private int visibleThreshold = 5;
    private boolean isLoading;
    private LinearLayoutManager linearLayoutManager;

    public AdapterPosters(Context context, List<Results> itens, RecyclerView recyclerView)
    {
        this.context = context;
        this.itens = itens;

        System.out.println(recyclerView.getLayoutManager());
        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            int orientation = context.getResources().getConfiguration().orientation;
            View view;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                view = LayoutInflater.from(context).inflate(R.layout.adapter_filmes, parent, false);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.adapter_filmes_land, parent, false);
            }
            return new ViewHolderPosters(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderPosters) {
            final ViewHolderPosters aux = (ViewHolderPosters) holder;
            aux.txtTitulo.setText(itens.get(position).getTitle());
            NumberFormat nf = new DecimalFormat("##.###");
            aux.txtMedia.setText(context.getString(R.string.mediaAvaliacoes, nf.format(itens.get(position).getVote_average())));
            if (itens.get(position).getOverview().equalsIgnoreCase("No overview found.") || itens.get(position).getOverview().length() == 0) {
                aux.txtResumo.setText(context.getString(R.string.sinopse_indisponivel));
            } else {
                aux.txtResumo.setText(itens.get(position).getOverview());
            }
            if (itens.get(position).getPoster_path() != null) {
                try {
                    String str = "http://image.tmdb.org/t/p/" + Aplicacao.getConfigsApplication().getImages().getPoster_sizes().get(1) + itens.get(position).getPoster_path();
                    Glide.with(context)
                            .load(str)
                            .placeholder(android.R.drawable.progress_horizontal)
                            .skipMemoryCache(false)
                            .into(aux.imgPoster);
                } catch (Exception e) {
                    setTxtImgNaoEncontrada(aux);
                }
            } else {
                Glide.clear(aux.imgPoster);
                aux.imgPoster.setImageDrawable(null);
                setTxtImgNaoEncontrada(aux);
            }
        }
        else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    private void setTxtImgNaoEncontrada(ViewHolderPosters aux)
    {
        aux.txtImgIndisp.setVisibility(View.VISIBLE);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itens == null ? 0 : itens.size();
    }

    private OnLoadMoreListener onLoadMoreListener;
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return itens.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
}
