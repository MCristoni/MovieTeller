package com.example.mathe.movieteller.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mathe.movieteller.R;
import com.example.mathe.movieteller.activities.DetalhesFilmeActivity;
import com.example.mathe.movieteller.utils.Aplicacao;

public class ViewHolderPosters extends RecyclerView.ViewHolder  implements View.OnClickListener{

    final TextView txtImgIndisp;
    final TextView txtResumo;
    final TextView txtMedia;
    final TextView txtTitulo;
    final ImageView imgPoster;

    public ViewHolderPosters(View view)
    {
        super(view);
        txtImgIndisp = (TextView) view.findViewById(R.id.txtImgIndisp);
        txtResumo = (TextView) view.findViewById(R.id.txtResumo);
        txtMedia = (TextView) view.findViewById(R.id.txtMedia);
        txtTitulo = (TextView) view.findViewById(R.id.txtTituloGrupo);
        imgPoster = (ImageView) view.findViewById(R.id.imgPoster);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), DetalhesFilmeActivity.class);
        intent.putExtra("idFilme", Aplicacao.getRetornoApplication().getResults().get(getAdapterPosition()).getId()+"");
        view.getContext().startActivity(intent);
    }
}
