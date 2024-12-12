package com.example.skynav.Adaptadores;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skynav.POJO.VehiculoAereo;
import com.example.skynav.R;

import java.util.ArrayList;

public class Adaptador_Vehiculo extends ArrayAdapter {

    private Activity context;
    private ArrayList<VehiculoAereo>array;

    private Drawable imgVuelo;
    private int layoutPersonalizado;

    public Adaptador_Vehiculo(@NonNull Activity context, int layoutPersonalizado,ArrayList<VehiculoAereo>array,Drawable imgVuelo) {
        super(context, layoutPersonalizado,array);
        this.context=context;
       this.array=array;
        this.layoutPersonalizado=layoutPersonalizado;
        this.imgVuelo=imgVuelo;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View fila=convertView;
        Adaptador_Vehiculo.ViewHolder viewHolder;
        if(fila==null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            fila = layoutInflater.inflate(layoutPersonalizado, null);
            viewHolder = new Adaptador_Vehiculo.ViewHolder();
            viewHolder.origen=fila.findViewById(R.id.origen);
            viewHolder.destino=fila.findViewById(R.id.destino);
            viewHolder.tvTexto=fila.findViewById(R.id.tvTexto);
            viewHolder.imgVuelos=fila.findViewById(R.id.imgVuelos);
            viewHolder.tvPrecio=fila.findViewById(R.id.tvPrecio);
            viewHolder.tvPlazas=fila.findViewById(R.id.tvPlazas);


        }else{

            viewHolder= (Adaptador_Vehiculo.ViewHolder) fila.getTag();

        }

        TextView origen=fila.findViewById(R.id.origen);
        TextView destino=fila.findViewById(R.id.destino);
        ImageView imgVuelos=fila.findViewById(R.id.imgVuelos);
        TextView tvTexto = fila.findViewById(R.id.tvTexto);
        TextView tvPlazas=fila.findViewById(R.id.tvPlazas);
        TextView tvPrecio=fila.findViewById(R.id.tvPrecio);

        imgVuelos.setImageDrawable(imgVuelo);
        tvTexto.setText(array.get(position).getNombre().toString());
        tvPrecio.setText("Precio: "+array.get(position).getPrecio().toString()+"€");
        tvPlazas.setText("Plazas: "+array.get(position).getPlaza().toString());
        origen.setText(array.get(position).getOrigen().toString()+" >");
        destino.setText(array.get(position).getDestino().toString());

        return fila;
    }

    private static class ViewHolder{
        TextView tvTexto;
        TextView tvPrecio;
        TextView tvPlazas;
        TextView origen;
        TextView destino;
        ImageView imgVuelos;


    }


}