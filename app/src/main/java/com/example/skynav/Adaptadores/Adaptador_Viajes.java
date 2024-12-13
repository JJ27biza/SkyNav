package com.example.skynav.Adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.example.skynav.POJO.Viaje;
import com.example.skynav.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Adaptador_Viajes extends ArrayAdapter {
    private Activity context;
   private ArrayList<Viaje> arrayViaje= new ArrayList<Viaje>();
    private int layoutPersonalizado;
    public Adaptador_Viajes(@NonNull Activity context, int layoutPersonalizado, ArrayList<Viaje>arrayViaje) {
        super(context, layoutPersonalizado,arrayViaje);
        this.context=context;
        this.arrayViaje=arrayViaje;
        this.layoutPersonalizado=layoutPersonalizado;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View fila=convertView;
        Adaptador_Viajes.ViewHolder viewHolder;
        if(fila==null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            fila = layoutInflater.inflate(layoutPersonalizado, null);
            viewHolder = new Adaptador_Viajes.ViewHolder();
            viewHolder.tvAvion=fila.findViewById(R.id.avion);
            viewHolder.tvOrigen=fila.findViewById(R.id.tvorigen);
            viewHolder.tvDestino=fila.findViewById(R.id.tvdestino);
            viewHolder.tvFecha=fila.findViewById(R.id.fecha);


        }else{

            viewHolder= (Adaptador_Viajes.ViewHolder) fila.getTag();

        }

        TextView tvAvion=fila.findViewById(R.id.avion);
        TextView tvOrigen=fila.findViewById(R.id.tvorigen);
        TextView tvDestino=fila.findViewById(R.id.tvdestino);
        TextView tvFecha = fila.findViewById(R.id.fecha);



        tvAvion.setText(arrayViaje.get(position).getVehiculo().toString());
        tvOrigen.setText(arrayViaje.get(position).getOrigen().toString()+" >> ");
        tvDestino.setText(arrayViaje.get(position).getDestino().toString());
        tvFecha.setText(arrayViaje.get(position).getFecha().toString());

        return fila;
    }

    private static class ViewHolder{
        TextView tvAvion;
        TextView tvOrigen;
        TextView tvDestino;
        TextView tvFecha;



    }
}
