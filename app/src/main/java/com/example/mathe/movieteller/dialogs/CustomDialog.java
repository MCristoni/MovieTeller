package com.example.mathe.movieteller.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.mathe.movieteller.R;
import com.example.mathe.movieteller.activities.MainActivity;
import com.example.mathe.movieteller.utils.Aplicacao;

public class CustomDialog
{
    private Dialog dialog = null;
    private Context context = null;
    private Button button;
    private int idMaisPopulares;
    private int idMenosPopulares;
    private int idMelhoresAvaliados;
    private int idPioresAvaliados;

    public void ShowDialog(final Activity context, int selecionado)
    {
        dialog = new Dialog(context, R.style.dialog_theme);
        this.context = context;
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.modal);

        instanciarElementosDialog();

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        if (selecionado == -1) //-1 é o valor de controle
        {
            radioGroup.check(idMaisPopulares);
        }
        else
        {
            radioGroup.check(selecionado);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId)
            {
                boolean maisPopulares = (idMaisPopulares == checkedId);
                boolean menosPopulares = (idMenosPopulares == checkedId);
                boolean melhoresAvaliados = (idMelhoresAvaliados == checkedId);
                boolean pioresAvaliados = (idPioresAvaliados == checkedId);

                if (maisPopulares)
                {
                   realizarAcoes(group, idMaisPopulares, "popularity.desc", context.getString(R.string.mais_populares), radioGroup);
                    context.finish();
                }
                else if (menosPopulares)
                {
                    realizarAcoes(group, idMenosPopulares, "popularity.asc", context.getString(R.string.menos_populares), radioGroup);
                    context.finish();
                }
                else if (melhoresAvaliados)
                {
                    realizarAcoes(group, idMelhoresAvaliados, "vote_average.desc", context.getString(R.string.melhores_avalia_es), radioGroup);
                    context.finish();
                }
                else if (pioresAvaliados)
                {
                    realizarAcoes(group, idPioresAvaliados, "vote_average.asc", context.getString(R.string.piores_avalia_es), radioGroup);
                    context.finish();
                }
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    dialog.dismiss();
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        dialog.show();
    }

    private void realizarAcoes(RadioGroup group, int idSelecionado, String ordenacao, String titulo, RadioGroup radioGroup) {
        group.check(idSelecionado);
        Aplicacao.setIdRadioSelecionado(idSelecionado);
        Aplicacao.setRetornoApplication(null);
        Bundle parametros = new Bundle();
        parametros.putString("intent_ordenacao", ordenacao);
        parametros.putInt("intent_pagina", 1);
        parametros.putString("intent_titulo", titulo);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtras(parametros);
        radioGroup.setOnCheckedChangeListener(null);
        context.startActivity(intent);
        dialog.dismiss();
    }

    private void instanciarElementosDialog() {
        button = (Button) dialog.findViewById(R.id.btnCancelar);
        idMaisPopulares = R.id.radioMaisPopulares;
        idMenosPopulares = R.id.radioMenosPopulares;
        idMelhoresAvaliados = R.id.radioMelhoresAvaliados;
        idPioresAvaliados = R.id.radioPioresAvaliados;
    }
}