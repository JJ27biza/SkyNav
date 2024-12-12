package com.example.skynav;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.skynav.Adaptadores.AdaptadorTipos;
import com.example.skynav.Adaptadores.Adaptador_Vehiculo;
import com.example.skynav.Constantes.Constantes;
import com.example.skynav.DB.DBSkyNav;
import com.example.skynav.POJO.VehiculoAereo;

import java.util.ArrayList;


public class MainActivity3 extends AppCompatActivity {
private String tipo;

private Constantes constantes= new Constantes();


private ArrayList<VehiculoAereo> arrayVehiculo=new ArrayList<>();
private SQLiteDatabase db;
private ListView lv;
private  String texto="";
private Adaptador_Vehiculo adaptadorVehiculo;
private Spinner spOrigen;
private Spinner spDestino;
private TextView tvTitulo;
private ArrayAdapter<String> origen;
private  ArrayAdapter<String> destino;
  private Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        lv=findViewById(R.id.lvVehiculos);
        tvTitulo=findViewById(R.id.tvTitulo);
        spOrigen=findViewById(R.id.spOrigen);
        spDestino=findViewById(R.id.spDestino);
        inicializarBD();
        elementoRecogido();
        elementosVarios();
        creacionSpinner();
        creacionLista();
        actionSpinner();
        actionListView();






    }

    //Método que realiza las acciones del spinner dependiendo del origen y destino
    private void creacionSpinner() {
        switch (texto) {
            case "AVION":

                origen = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.origen_Avion));
                spOrigen.setAdapter(origen);
                destino = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.destino_Avion));
                spDestino.setAdapter(destino);


                break;
            case "AVIONETA":
                origen = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.origen_Avioneta));
                spOrigen.setAdapter(origen);
                destino = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.destino_Avioneta));
                spDestino.setAdapter(destino);

                break;
            case "GLOBO":
                origen = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.origen_n));
                spOrigen.setAdapter(origen);
                destino = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.destino_n));
                spDestino.setAdapter(destino);

                break;
            case "DIRIGIBLE":
                origen = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.origen_n));
                spOrigen.setAdapter(origen);
                destino = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.destino_n));
                spDestino.setAdapter(destino);

                break;
            case "HELICOPTERO":
                origen = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.origen_n));
                spOrigen.setAdapter(origen);
                destino = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.destino_n));
                spDestino.setAdapter(destino);

                break;


        }
    }

//Método para finalizar la activity
    public void OnclickVolver(View view) {
        finish();
    }
    //Este método añade contenido a tvTitulo
    private String elementoRecogido(){

        Intent intent = getIntent();
        tipo =intent.getStringExtra("tipo");

        switch (tipo){
            case "Aviones":
                texto="AVION";

                tvTitulo.setText(tipo);

                break;
            case "Avionetas":
                texto="AVIONETA";
                tvTitulo.setText(tipo);
                break;
            case "Globos Aerostaticos":
                texto="GLOBO";
                tvTitulo.setText(tipo);
                break;
            case "Dirigibles":
                texto="DIRIGIBLE";
                tvTitulo.setText(tipo);
                break;
            case"Helicopteros":
                texto="HELICOPTERO";
                tvTitulo.setText(tipo);
                break;
        }
        return texto;

    }
//Método para realizar consulta en la base de datos dependiendo del tvTitulo
    private void elementosVarios() {
        String[] recuperarConsultas = {constantes.vehiculo_column2, constantes.vehiculo_column4, constantes.vehiculo_column5,constantes.vehiculo_column6,constantes.vehiculo_column7};
        Cursor cursor = db.query(constantes.DB_tablename_2, recuperarConsultas, constantes.vehiculo_column3 + "=" + "'"+elementoRecogido()+"'", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(0);
                String precio = cursor.getString(1);
                String plaza = cursor.getString(2);
                String origen=cursor.getString(3);
                String destino=cursor.getString(4);
                VehiculoAereo vehiculoAereo= new VehiculoAereo(nombre,precio,plaza,origen,destino);
                arrayVehiculo.add(vehiculoAereo);
            } while (cursor.moveToNext());
        } else {
            AlertDialog.Builder ventana=  new AlertDialog.Builder(this);
            ventana.setIcon(R.drawable.travel1);
            ventana.setMessage("No se han encontrado resultados");
            ventana.setTitle("Información");

            ventana.show();
            spOrigen.setSelection(0);
            spDestino.setSelection(0);
        }
        cursor.close();
    }

    //Método para realizar consulta de Origen en la base de datos dependiendo del tvTitulo
    private void elementosOrigen(String pais) {
        String[] recuperarConsultas = {constantes.vehiculo_column2, constantes.vehiculo_column4, constantes.vehiculo_column5,constantes.vehiculo_column6,constantes.vehiculo_column7};
        Cursor cursor = db.query(constantes.DB_tablename_2, recuperarConsultas, constantes.vehiculo_column3 + "=" + "'"+elementoRecogido()+"'"+" AND "+constantes.vehiculo_column6 + "=" + "'"+pais+"'", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(0);
                String precio = cursor.getString(1);
                String plaza = cursor.getString(2);
                String origen=cursor.getString(3);
                String destino=cursor.getString(4);
                VehiculoAereo vehiculoAereo= new VehiculoAereo(nombre,precio,plaza,origen,destino);
                arrayVehiculo.add(vehiculoAereo);
            } while (cursor.moveToNext());
        } else {
            AlertDialog.Builder ventana=  new AlertDialog.Builder(this);
            ventana.setIcon(R.drawable.travel1);
            ventana.setMessage("No se han encontrado resultados");
            ventana.setTitle("Información");
            spOrigen.setSelection(0);
            spDestino.setSelection(0);

            ventana.show();
        }
        cursor.close();
    }

    //Método para realizar consulta de Destino en la base de datos dependiendo del tvTitulo
    private void elementosDestino(String pais) {
        String[] recuperarConsultas = {constantes.vehiculo_column2, constantes.vehiculo_column4, constantes.vehiculo_column5,constantes.vehiculo_column6,constantes.vehiculo_column7};
        Cursor cursor = db.query(constantes.DB_tablename_2, recuperarConsultas, constantes.vehiculo_column3 + "=" + "'"+elementoRecogido()+"'"+" AND "+constantes.vehiculo_column7 + "=" + "'"+pais+"'", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(0);
                String precio = cursor.getString(1);
                String plaza = cursor.getString(2);
                String origen=cursor.getString(3);
                String destino=cursor.getString(4);
                VehiculoAereo vehiculoAereo= new VehiculoAereo(nombre,precio,plaza,origen,destino);
                arrayVehiculo.add(vehiculoAereo);
            } while (cursor.moveToNext());
        } else {
            AlertDialog.Builder ventana=  new AlertDialog.Builder(this);
            ventana.setIcon(R.drawable.travel1);
            ventana.setMessage("No se han encontrado resultados");
            ventana.setTitle("Información");

            ventana.show();
            spOrigen.setSelection(0);
            spDestino.setSelection(0);
        }
        cursor.close();
    }
    //Método para realizar consulta de Origen y Destino en la base de datos dependiendo del tvTitulo
    private void elementoOrigenDestino(String destino1,String origen1){

        String[] recuperarConsultas = {constantes.vehiculo_column2, constantes.vehiculo_column4, constantes.vehiculo_column5,constantes.vehiculo_column6,constantes.vehiculo_column7};
        Cursor cursor = db.query(constantes.DB_tablename_2, recuperarConsultas, constantes.vehiculo_column3 + "=" + "'"+elementoRecogido()+"'"+" AND "+constantes.vehiculo_column7 + "=" + "'"+destino1+"'"+"AND "+constantes.vehiculo_column6+"=" + "'"+origen1+"'", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(0);
                String precio = cursor.getString(1);
                String plaza = cursor.getString(2);
                String origen=cursor.getString(3);
                String destino=cursor.getString(4);
                VehiculoAereo vehiculoAereo= new VehiculoAereo(nombre,precio,plaza,origen,destino);
                arrayVehiculo.add(vehiculoAereo);
            } while (cursor.moveToNext());
        } else {
            AlertDialog.Builder ventana=  new AlertDialog.Builder(this);
            ventana.setIcon(R.drawable.travel1);
            ventana.setMessage("No se han encontrado resultados");
            ventana.setTitle("Información");

            ventana.show();
            spOrigen.setSelection(0);
            spDestino.setSelection(0);
        }
        cursor.close();
    }



    //Método para adaptar la listview dependiendo del texto
    private void creacionLista(){
            switch (texto){
                case "AVION":


                    adaptadorVehiculo= new Adaptador_Vehiculo(MainActivity3.this,R.layout.activity_adaptador_vehiculo,arrayVehiculo,getResources().getDrawable(R.drawable.avion2));
                    lv.setAdapter(adaptadorVehiculo);


                    break;
                case "AVIONETA":

                    adaptadorVehiculo= new Adaptador_Vehiculo(MainActivity3.this,R.layout.activity_adaptador_vehiculo,arrayVehiculo,getResources().getDrawable(R.drawable.avioneta2));
                    lv.setAdapter(adaptadorVehiculo);
                    break;
                case "GLOBO":

                    adaptadorVehiculo= new Adaptador_Vehiculo(MainActivity3.this,R.layout.activity_adaptador_vehiculo,arrayVehiculo,getResources().getDrawable(R.drawable.globo2));
                    lv.setAdapter(adaptadorVehiculo);
                    break;
                case "DIRIGIBLE":

                    adaptadorVehiculo= new Adaptador_Vehiculo(MainActivity3.this,R.layout.activity_adaptador_vehiculo,arrayVehiculo,getResources().getDrawable(R.drawable.cepelin));
                    lv.setAdapter(adaptadorVehiculo);
                    break;
                case"HELICOPTERO":

                    adaptadorVehiculo= new Adaptador_Vehiculo(MainActivity3.this,R.layout.activity_adaptador_vehiculo,arrayVehiculo,getResources().getDrawable(R.drawable.helicoptero2));
                    lv.setAdapter(adaptadorVehiculo);
                    break;

            }

    }
    //Método para hacer el shared preference donde se guarda el nombre origen y destino
    private void guardarNombre(String nombre,String origen,String destino){

        SharedPreferences sharedPreferences = getSharedPreferences("Avion", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nombre",nombre);
        editor.putString("origen",origen);
        editor.putString("destino",destino);
        editor.apply();
    }
    //Método para inicializar la base de datos
    private void inicializarBD(){
        try {
            DBSkyNav dbSkyNav = new DBSkyNav(MainActivity3.this);
            db = dbSkyNav.getWritableDatabase();
        }catch (Exception e){
            Log.println(Log.ASSERT,"Error",""+e.getMessage());
        }
    }
    //Método para realizar acciones al utilizar los spinners si encuentra datos los muestra en la listview
    //si no aparece un dialog como que no se a encontrado datos
    private void actionSpinner(){
        spOrigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String pais=parent.getItemAtPosition(position).toString();
                if(pais.equals("")&& spDestino.getSelectedItem().equals("")){
                    adaptadorVehiculo.clear();
                    adaptadorVehiculo.notifyDataSetChanged();
                    arrayVehiculo.clear();
                    elementoRecogido();
                    elementosVarios();
                    creacionLista();



                }
                else if(spDestino.getSelectedItem().equals("")){
                    adaptadorVehiculo.clear();
                    adaptadorVehiculo.notifyDataSetChanged();
                    arrayVehiculo.clear();
                    elementosOrigen(pais);
                    creacionLista();





                }else if(pais.equals("")&&!(spDestino.getSelectedItem().equals(""))){
                    adaptadorVehiculo.clear();
                    adaptadorVehiculo.notifyDataSetChanged();
                    arrayVehiculo.clear();
                    elementosDestino(spDestino.getSelectedItem().toString());
                    creacionLista();

                }

                else if(!(pais.equals("")) && !(spDestino.getSelectedItem().equals(""))){
                    adaptadorVehiculo.clear();
                    adaptadorVehiculo.notifyDataSetChanged();
                    arrayVehiculo.clear();
                    elementoOrigenDestino(spDestino.getSelectedItem().toString(),pais);
                    creacionLista();



                }






            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String pais=parent.getItemAtPosition(position).toString();
                if(pais.equals("") && spOrigen.getSelectedItem().equals("")){
                    adaptadorVehiculo.clear();
                    adaptadorVehiculo.notifyDataSetChanged();
                    arrayVehiculo.clear();
                    elementoRecogido();
                    elementosVarios();
                    creacionLista();


                }else if(spOrigen.getSelectedItem().equals("")){
                    adaptadorVehiculo.clear();
                    adaptadorVehiculo.notifyDataSetChanged();
                    arrayVehiculo.clear();
                    elementosDestino(pais);
                    creacionLista();
                }
                else if(pais.equals("")&&!(spOrigen.getSelectedItem().equals(""))){
                    adaptadorVehiculo.clear();
                    adaptadorVehiculo.notifyDataSetChanged();
                    arrayVehiculo.clear();
                    elementosOrigen(spOrigen.getSelectedItem().toString());
                    creacionLista();
                }

                else if(!(pais.equals("")) && !(spOrigen.getSelectedItem().equals(""))){
                    adaptadorVehiculo.clear();
                    adaptadorVehiculo.notifyDataSetChanged();
                    arrayVehiculo.clear();
                    elementoOrigenDestino(pais,spOrigen.getSelectedItem().toString());
                    creacionLista();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
    }
    //Método para realizar acciones en la listview cambiando de activity
    private void actionListView() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String elemento=parent.getItemAtPosition(position).toString();
                String precio=arrayVehiculo.get(position).getPrecio().toString();
                String plazas=arrayVehiculo.get(position).getPlaza().toString();
                String nombre=arrayVehiculo.get(position).getNombre().toString();
                String origen=arrayVehiculo.get(position).getOrigen().toString();
                String destino=arrayVehiculo.get(position).getDestino().toString();
                guardarNombre(nombre,origen,destino);
                intent=new Intent();

                intent.setClass(MainActivity3.this, MainActivity4.class);
                intent.putExtra("tipo",elemento);
                intent.putExtra("precio",precio);
                intent.putExtra("plazas",plazas);
                startActivity(intent);

            }
        });
    }
    //Método que cierra la base de datos
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }


}