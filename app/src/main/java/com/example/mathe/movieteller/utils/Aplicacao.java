package com.example.mathe.movieteller.utils;

import android.app.Application;

import com.example.mathe.movieteller.adapters.AdapterPosters;
import com.example.mathe.movieteller.classes.Configs;
import com.example.mathe.movieteller.classes.Retorno;

public class Aplicacao extends Application
{
    private static Retorno retornoApplication;
    private static Configs configsApplication;
    public static final String chaveAPI = "";
    private static int idRadioSelecionado = -1;

    @Override
    public void onCreate() {
        retornoApplication = null;
        super.onCreate();
    }

    public static Retorno getRetornoApplication() {
        return retornoApplication;
    }

    public static void setRetornoApplication(Retorno retornoApplication) {
        Aplicacao.retornoApplication = retornoApplication;
    }

    public static Configs getConfigsApplication() {
        return configsApplication;
    }

    public static void setConfigsApplication(Configs configsApplication) {
        Aplicacao.configsApplication = configsApplication;
    }

    public static int getIdRadioSelecionado() {
        return idRadioSelecionado;
    }

    public static void setIdRadioSelecionado(int idRadioSelecionado) {
        Aplicacao.idRadioSelecionado = idRadioSelecionado;
    }
}
