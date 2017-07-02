package com.example.mathe.movieteller.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.mathe.movieteller.R;
import com.example.mathe.movieteller.adapters.AdapterPosters;
import com.example.mathe.movieteller.classes.Results;
import com.example.mathe.movieteller.classes.Retorno;
import com.example.mathe.movieteller.listeners.OnLoadMoreListener;
import com.example.mathe.movieteller.utils.Aplicacao;
import com.example.mathe.movieteller.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import livroandroid.lib.utils.HttpHelper;

public class MainActivity extends BaseActivity {
    public static final String TAG = "movieTeller";
    public RecyclerView recycleView;
    public AdapterPosters adapter;
    public List<Results> result;
    public int tamanhoNovo;
    public int pagina;
    public String ordenacao;
    public boolean carregados = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instanciaElementosTela();
        setContextAtual(this);
        setContextMain(this);
        setUpToolbar();

        if (savedInstanceState != null)
        {
            Log.d(TAG, "Instância salva NÃO É NULA");
            tamanhoNovo = savedInstanceState.getInt("tamanhoNovo");
            pagina = savedInstanceState.getInt("pagina");
            ordenacao = savedInstanceState.getString("ordenacao");
            carregados = savedInstanceState.getBoolean("carregados");
            recycleView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable("teste"));
        }
        else
        {
            Log.d(TAG, "Instância salva é NULA");
            pegarFilmesServico();
        }
        setarAdapter();
        ouvirMudançasAdapter();

    }

    public void ouvirMudançasAdapter() {
        if (carregados) {
            adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    recycleView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (result.size() <= (tamanhoNovo)) {
                                result.add(null);
                                adapter.notifyItemInserted(result.size() - 1);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        result.remove(result.size() - 1);
                                        adapter.notifyItemRemoved(result.size());

                                        Log.d(TAG, "Passou aqui");

                                        if (pagina < Aplicacao.getRetornoApplication().getTotal_pages())
                                        pagina += 1;
                                        pegarFilmesServico();

                                        adapter.notifyDataSetChanged();
                                        adapter.setLoaded();
                                        tamanhoNovo = Aplicacao.getRetornoApplication().getResults().size();
                                    }

                                }, 3500);
                            }
                        }
                    });
                }
            });
        }
        else
        {
            Snackbar snack = Snackbar.make(recycleView, getString(R.string.internet), Snackbar.LENGTH_INDEFINITE);
            snack.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.primaryDark));
            snack.setActionTextColor(Color.WHITE);
            snack.show();
        }
    }

    public void setarAdapter() {
        if (Aplicacao.getRetornoApplication() != null) {
            adapter = new AdapterPosters(this, Aplicacao.getRetornoApplication().getResults(), recycleView);
            recycleView.setAdapter(adapter);
            result = Aplicacao.getRetornoApplication().getResults();
        }
        else
        {
            Log.d(TAG, "RETORNO NULL");
        }
    }

    public void pegarFilmesServico() {

        if (isOnline()) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpHelper http = new HttpHelper();
                    String str;
                    try {
                        //Optei por não pegar os títulos em português pois muitos não tem sinopse
                        str = http.doGet("http://api.themoviedb.org/3/discover/movie?api_key=" + Aplicacao.chaveAPI + "&sort_by=" + ordenacao + "&page=" + pagina + "&language=en-US");
                        Gson gson = new Gson();
                        new TypeToken<Retorno>() {
                        }.getType();
                        Retorno itensRetorno = gson.fromJson(str, Retorno.class);
                        if (Aplicacao.getRetornoApplication() == null) {
                            Aplicacao.setRetornoApplication(itensRetorno);
                        } else {
                            for (Results item : itensRetorno.getResults()) {
                                Aplicacao.getRetornoApplication().getResults().add(item);
                            }
                        }
                        tamanhoNovo = Aplicacao.getRetornoApplication().getResults().size();
                        Log.d(TAG, "Tamanho Results: " + Aplicacao.getRetornoApplication().getResults().size());
                        carregados = true;
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

    }

    private void instanciaElementosTela()
    {
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        tamanhoNovo = 0;

        Intent intent = getIntent();
        Bundle args = intent.getExtras();

        try {
            pagina = args.getInt("intent_pagina");
        }
        catch (Exception e)
        {
            pagina = 1;
        }

        try{
            ordenacao = args.getString("intent_ordenacao");
        }
        catch (Exception e)
        {
            ordenacao = "popularity.desc";
        }

        try{
            this.setTitle(args.getString("intent_titulo"));
        }
        catch (Exception e)
        {
            this.setTitle(R.string.app_name);
        }

        recycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Salva estado do recycleView
        outState.putParcelable("teste", recycleView.getLayoutManager().onSaveInstanceState());
        outState.putInt("pagina", pagina);
        outState.putString("ordenacao", ordenacao);
        outState.putInt("tamanhoNovo", tamanhoNovo);
        outState.putBoolean("carregados", carregados);
        super.onSaveInstanceState(outState);
    }
}
