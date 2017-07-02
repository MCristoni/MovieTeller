package com.example.mathe.movieteller.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mathe.movieteller.R;
import com.example.mathe.movieteller.activities.MainActivity;
import com.example.mathe.movieteller.dialogs.CustomDialog;

public class BaseActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private Toolbar mToolbar;
    private Context contextAtual;
    private Context contextMain;

    protected void setUpToolbar()
    {
         mToolbar = (Toolbar) findViewById(R.id.tb_main);
        if (mToolbar != null)
        {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.ic_ordenar:
                CustomDialog dialog = new CustomDialog();
                dialog.ShowDialog(this, Aplicacao.getIdRadioSelecionado());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        menu.clear();
        if (contextAtual == contextMain) {
            getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        if (contextAtual == contextMain) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Snackbar snack = Snackbar.make(mToolbar, getString(R.string.sair), Snackbar.LENGTH_SHORT);
            snack.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.primaryDark));
            snack.setActionTextColor(Color.WHITE);
            snack.show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        //Aplicacao.setRetornoApplication(null);
        super.onDestroy();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void setContextAtual(Context contextAtual) {
        this.contextAtual = contextAtual;
    }

    public void setContextMain(Context contextMain) {
        this.contextMain = contextMain;
    }
}
