package com.example.skynav.Adaptadores;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skynav.R;

public class AdaptadorTipos extends ArrayAdapter {

    private Activity context;
    private String[] arrayTexto;

    private TypedArray arrayVuelos;
    private int layoutPersonalizado;

    public AdaptadorTipos(@NonNull Activity context, int layoutPersonalizado, String[] arrayTexto, TypedArray arrayVuelos) {
        super(context, layoutPersonalizado,arrayTexto);
        this.context = context;
        this.layoutPersonalizado = layoutPersonalizado;
        this.arrayTexto = arrayTexto;
        this.arrayVuelos = arrayVuelos;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View fila=convertView;
        ViewHolder viewHolder;
        if(fila==null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            fila = layoutInflater.inflate(layoutPersonalizado, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTexto=fila.findViewById(R.id.tvTexto);
            viewHolder.imgVuelos=fila.findViewById(R.id.imgVuelos);


        }else{

            viewHolder= (ViewHolder) fila.getTag();

        }


        TextView tvTexto = fila.findViewById(R.id.tvTexto);
        ImageView imgVuelos = fila.findViewById(R.id.imgVuelos);

        tvTexto.setText(arrayTexto[position]);
        imgVuelos.setImageDrawable(arrayVuelos.getDrawable(position));

        if (position % 2 == 1) {
            fila.setBackgroundColor(Color.rgb(255, 255, 255));
        } else {
            fila.setBackgroundColor(Color.rgb( 161, 218, 225));

        }


        return fila;
    }

    private static class ViewHolder{
        TextView tvTexto;
       ImageView imgVuelos;


    }
}