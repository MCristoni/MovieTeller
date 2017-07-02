package com.example.mathe.movieteller.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mathe.movieteller.activities.MainActivity;
import com.example.mathe.movieteller.R;
import com.example.mathe.movieteller.tasks.GetConfigsTask;

public class SplashScreenActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        taskRetorno();
                    }
                }, 1000);
    }

    private void taskRetorno()
    {
        GetConfigsTask task = new GetConfigsTask();
        task.execute("http://api.themoviedb.org/3/configuration?api_key="+ Aplicacao.chaveAPI);
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }
}
