package com.example.mathe.movieteller.tasks;

import android.os.AsyncTask;

import com.example.mathe.movieteller.classes.Configs;
import com.example.mathe.movieteller.utils.Aplicacao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import livroandroid.lib.utils.HttpHelper;

public class GetConfigsTask extends AsyncTask<String, Void, Boolean>
{
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(String... params) {
        HttpHelper http = new HttpHelper();
        String str;
        try {
            str = http.doGet(params[0]);
            Gson gson = new Gson();
            new TypeToken<Configs>(){}.getType();
            Configs teste = gson.fromJson(str, Configs.class);
            Aplicacao.setConfigsApplication(teste);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
