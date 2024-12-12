package com.example.skynav;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.skynav.Adaptadores.Adaptador_Vehiculo;
import com.example.skynav.Adaptadores.Adaptador_Viajes;
import com.example.skynav.Constantes.Constantes;
import com.example.skynav.DB.DBSkyNav;
import com.example.skynav.POJO.Viaje;

import java.util.ArrayList;

public class MainActivity7 extends AppCompatActivity {

    private ListView lvCompras;

    private ArrayList<Viaje>arrayViaje= new ArrayList<>();
    private SharedPreferences sharedPreferences2;
    private Constantes constantes= new Constantes();
    private SQLiteDatabase db;

    private  int idRecibido;
    private Adaptador_Viajes adaptadorViajes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lvCompras=findViewById(R.id.lvCompras);
        limpiar();
        inicializarBD();
        obteneridUsuario();
        obtenerViaje(idRecibido);
        adaptadorViajes= new Adaptador_Viajes(MainActivity7.this,R.layout.activity_adaptador_viajes,arrayViaje);
        lvCompras.setAdapter(adaptadorViajes);
        lvBorrar();
    }
    //Método para obtener el id del Usuario
    private void obteneridUsuario(){
        sharedPreferences2 = getSharedPreferences("MisPreferenciasDB", MODE_PRIVATE);
        idRecibido = sharedPreferences2.getInt("id", 0);

    }
//Este método permite obtener el viaje al obtener el id del Usuario
    private void obtenerViaje(int idRecibido){

        String[] recuperarConsultas = {constantes.viajes_column2, constantes.viajes_column3,constantes.viajes_column4,constantes.viajes_column5,constantes.viajes_id};
        Cursor cursor = db.query(constantes.DB_tablename_3, recuperarConsultas, constantes.viajes_column1+"="+idRecibido, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String avion = cursor.getString(0);
                String fecha=cursor.getString(1);
                String origen=cursor.getString(2);
                String destino=cursor.getString(3);
                int id=cursor.getInt(4);
                Viaje viaje= new Viaje(avion,fecha,origen,destino,id);
                arrayViaje.add(viaje);
               /* arrayId.add(id);
                arrayAvion.add(avion);
                arrayFecha.add(fecha);
                arrayOrigen.add(origen);
                arrayDestino.add(destino);*/


            } while (cursor.moveToNext());
        } else {
            AlertDialog.Builder ventana=  new AlertDialog.Builder(this);
            ventana.setIcon(R.drawable.travel1);
            ventana.setMessage("No se han encontrado resultados");
            ventana.setTitle("Información");

            ventana.show();
        }
        cursor.close();



    }

    //Método para finalizar la activity
    public void onClickVolver(View view) {
        finish();
    }
    //Método que permite borrar un viaje al clickar en el viaje
    private void lvBorrar(){
        lvCompras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder ventana=  new AlertDialog.Builder(MainActivity7.this);
                ventana.setIcon(R.drawable.travel1);
                ventana.setMessage("Quieres borrar este usuario\n¿Estas seguro?");
                ventana.setTitle("Borrado");
                ventana.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        //todo operaciones correspondientes
                        borradoViajeDB(arrayViaje.get(position).getId());
                        arrayViaje.remove(position);
                        adaptadorViajes.notifyDataSetChanged();
                    }
                });
                ventana.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ventana.show();

            }
        });

    }
    //Método para borrar el viaje en la base de datos
    private void borradoViajeDB(int id){
        int i =db.delete(constantes.DB_tablename_3,constantes.viajes_id+"="+id,null);
        if(i==0){

            Toast.makeText(this, "Error en el borrado",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Borrado completado", Toast.LENGTH_SHORT).show();

        }


    }
    //Este método limpiar el array de arrayViaje
    private void limpiar(){
       arrayViaje.clear();
    }
    //Método para inicializar la base de datos
    private void inicializarBD(){

        try {
            DBSkyNav dbSkyNav = new DBSkyNav(MainActivity7.this);
            db = dbSkyNav.getWritableDatabase();
        }catch (Exception e){
            Log.println(Log.ASSERT,"Error",""+e.getMessage());
        }
    }
    //Método de destrucción para cerrar la base de datos
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}