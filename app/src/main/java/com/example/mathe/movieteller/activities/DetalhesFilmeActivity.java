package com.example.mathe.movieteller.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mathe.movieteller.R;
import com.example.mathe.movieteller.adapters.RecyclerAdapter;
import com.example.mathe.movieteller.classes.DetalhesFilmes;
import com.example.mathe.movieteller.classes.ResultsTrailer;
import com.example.mathe.movieteller.classes.RetornoTrailers;
import com.example.mathe.movieteller.classes.Videos;
import com.example.mathe.movieteller.utils.Aplicacao;
import com.example.mathe.movieteller.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import livroandroid.lib.utils.HttpHelper;

public class DetalhesFilmeActivity extends BaseActivity {

    private boolean carregados = false;
    private String idFilme;
    private ImageView imgBackdrop;
    private TextView txtTagline;
    private TextView txtNomeFilme;
    private TextView txtAvaliacoes;
    private ProgressDialog progress;
    private boolean possuiTrailer = false;
    private RetornoTrailers retornoTrailers;
    private DetalhesFilmes detalhesFilmes;
    private int tamX;
    private int tamY;
    private TextView txtSinopse;
    private TextView txtDuracao;
    private TextView txtGeneros;
    private RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_filme);
        this.setTitle("");
        setContextAtual(this);
        setUpToolbar();
        instanciaElementosTela();
        pegarDetalhesFilmeServico();
        if (carregados)
        {
            pegarTrailersServico();
            try {
                calculaTamanhoTela();
                carregaTela();
            } catch (InterruptedException e) {
                carregaModal(e.getMessage());
            }
        }
        else
            carregaModal(detalhesFilmes.getStatus_message());

        if (progress.isShowing() && progress != null)
        {
            progress.dismiss();
        }

    }

    private void carregaModal(String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Error");
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }

    private void calculaTamanhoTela() throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Point point = new Point();
                getWindowManager().getDefaultDisplay().getSize(point);
                tamX = point.x;
                tamY = (int)(tamX/1.7751479289940828402366863905325);//Proporção entre largura e altura das imagens baixadas
            }
        });
        t.start();
        t.join();
        setaTamanhoCorretoImgView();
    }

    private void setaTamanhoCorretoImgView() {
        imgBackdrop.setMaxWidth(tamX);
        imgBackdrop.setMinimumWidth(tamX);
        imgBackdrop.setMaxHeight(tamY);
        imgBackdrop.setMinimumHeight(tamY);
    }

    private void carregaTela()
    {
        try {
            String str = "http://image.tmdb.org/t/p/" + Aplicacao.getConfigsApplication().getImages().getBackdrop_sizes().get(1) + detalhesFilmes.getBackdrop_path();
            Glide.with(this)
                    .load(str)
                    .placeholder(android.R.drawable.progress_horizontal)
                    .skipMemoryCache(false)
                    .into(imgBackdrop);
        } catch (Exception e) {
            imgBackdrop.setVisibility(View.GONE);
            txtTagline.setVisibility(View.GONE);
        }

        if(detalhesFilmes.getTagline().equalsIgnoreCase(""))
            txtTagline.setVisibility(View.GONE);
        else
            txtTagline.setText(detalhesFilmes.getTagline());

        if(detalhesFilmes.getTitle().equalsIgnoreCase(""))
            txtNomeFilme.setVisibility(View.GONE);
        else if (detalhesFilmes.getRelease_date().equalsIgnoreCase(""))
            txtNomeFilme.setText(detalhesFilmes.getTitle());
        else
            txtNomeFilme.setText(detalhesFilmes.getTitle() + " (" + detalhesFilmes.getRelease_date().substring(0, 4)+")");

        this.setTitle(detalhesFilmes.getTitle());

        txtDuracao.setText(detalhesFilmes.getRuntime() + " min");

        if(detalhesFilmes.getGenres().size() == 0)
            txtGeneros.setVisibility(View.GONE);
        else {
            String str = "";
            for (int i=0; i < detalhesFilmes.getGenres().size(); i++)
            {
                if (i == detalhesFilmes.getGenres().size()-1)
                    str += detalhesFilmes.getGenres().get(i).getName();
                else
                    str += detalhesFilmes.getGenres().get(i).getName() + " | ";
            }
            txtGeneros.setText(str);
        }

        if (detalhesFilmes.getOverview().equalsIgnoreCase(""))
            txtSinopse.setVisibility(View.GONE);
        else
            txtSinopse.setText(detalhesFilmes.getOverview());

        if(detalhesFilmes.getTagline().equalsIgnoreCase(""))
            txtTagline.setVisibility(View.GONE);
        else
            txtTagline.setText(detalhesFilmes.getTagline());

        NumberFormat nf = new DecimalFormat("##.###");
        txtAvaliacoes.setText(getString(R.string.avaliacoes, nf.format(detalhesFilmes.getVote_average())));

        if (possuiTrailer)
        {
            carregarTrailers();
        }
    }

    private void carregarTrailers() {
        List<Videos> videos = new ArrayList<>();
        List<ResultsTrailer> trailer = new ArrayList<>();
        List<ResultsTrailer> teaser = new ArrayList<>();
        List<ResultsTrailer> others = new ArrayList<>();
        for (int i=0; i < retornoTrailers.getResults().size(); i++)
        {
            if (retornoTrailers.getResults().get(i).getType().equalsIgnoreCase("Trailer"))
            {
                trailer.add(retornoTrailers.getResults().get(i));
            }
            else if (retornoTrailers.getResults().get(i).getType().equalsIgnoreCase("Teaser"))
            {
                teaser.add(retornoTrailers.getResults().get(i));
            }
            else
            {
                others.add(retornoTrailers.getResults().get(i));
            }
        }

        if (trailer.size() > 0)
            videos.add(new Videos(trailer.get(0).getType(), trailer));

        if (teaser.size() > 0)
            videos.add(new Videos(teaser.get(0).getType(), teaser));

        if (others.size() > 0)
            videos.add(new Videos("Others", others));

        ButterKnife.bind(this);
        RecyclerAdapter adapter = new RecyclerAdapter(videos);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(adapter);
        NestedScrollView mainScrollView = (NestedScrollView)findViewById(R.id.mainScrollView);
        mainScrollView.smoothScrollTo(0,0);
    }

    private void pegarTrailersServico() {
        if (isOnline()) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpHelper http = new HttpHelper();
                    String str;
                    try {
                        str = http.doGet("http://api.themoviedb.org/3/movie/"+ idFilme +"/videos?api_key=" + Aplicacao.chaveAPI +"&language=en-US");
                        Gson gson = new Gson();
                        new TypeToken<RetornoTrailers>() {}.getType();
                        retornoTrailers = gson.fromJson(str, RetornoTrailers.class);
                        if (retornoTrailers.getStatus_message() == null && retornoTrailers.getStatus_code() == 0)
                            possuiTrailer = true;
                        else
                            possuiTrailer = false;
                    } catch (IOException e) {
                        possuiTrailer = false;
                    }
                }
            });
            t.start();
            try{
                t.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {
            possuiTrailer = false;
        }
        Log.d(MainActivity.TAG, "possuiTrailer: " + possuiTrailer);
    }

    public boolean pegarDetalhesFilmeServico() {
        if (isOnline()) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpHelper http = new HttpHelper();
                    String str;
                    try {
                        Log.d(MainActivity.TAG, "idFilme: " + idFilme);
                        str = http.doGet("https://api.themoviedb.org/3/movie/"+ idFilme +"?api_key=" + Aplicacao.chaveAPI +"&language=en-US");
                        Log.d(MainActivity.TAG, "str: " + str);
                        Gson gson = new Gson();
                        new TypeToken<DetalhesFilmes>() {}.getType();
                        detalhesFilmes = gson.fromJson(str, DetalhesFilmes.class);
                        if (detalhesFilmes.getStatus_message() == null && detalhesFilmes.getStatus_code() == 0)
                            carregados = true;
                        else
                            carregados = false;
                    } catch (IOException e) {
                        carregados = false;
                    }
                }
            });
            t.start();
            try{
                t.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {
            carregados = false;
        }

        Log.d(MainActivity.TAG, "carregados: " + carregados);
        return carregados;
    }

    private void instanciaElementosTela()
    {
        progress = ProgressDialog.show(this, getString(R.string.carregando), getString(R.string.favor_aguardar));
        Intent intent = getIntent();
        try {
            idFilme = intent.getStringExtra("idFilme");
        }
        catch (Exception e)
        {
            idFilme = null;
        }

        imgBackdrop = (ImageView) findViewById(R.id.imgBackdrop);
        txtAvaliacoes = (TextView) findViewById(R.id.txtAvaliacoes);
        txtTagline = (TextView) findViewById(R.id.txtTagline);
        txtNomeFilme = (TextView) findViewById(R.id.txtNomeFilme);
        txtSinopse = (TextView) findViewById(R.id.txtSinopse);
        txtDuracao = (TextView) findViewById(R.id.txtDuracao);
        txtGeneros = (TextView) findViewById(R.id.txtGeneros);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerViewVideos);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
