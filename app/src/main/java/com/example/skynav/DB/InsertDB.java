package com.example.skynav.DB;

import static android.view.View.GONE;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;

import com.example.skynav.Constantes.Constantes;
import com.example.skynav.MainActivity;

public class InsertDB {
    private ContentValues resgitroNuevo = new ContentValues();
    private Constantes constantes= new Constantes();
    private SQLiteDatabase db;
    Context mainActivity;




    public InsertDB(Context activity,SQLiteDatabase dbMain) {
        this.mainActivity=activity;
        this.db=dbMain;
    }

    public long insertarUsuario(String nombre, String usuario, String contraseña){

        if(nombre.isEmpty()||usuario.isEmpty()||contraseña.isEmpty()){
            Toast.makeText(mainActivity, "Campo vacio", Toast.LENGTH_SHORT).show();
        }else{
            try{
                resgitroNuevo.put(constantes.colum2,nombre);
                resgitroNuevo.put(constantes.colum3,usuario);
                resgitroNuevo.put(constantes.colum4,contraseña);
                long l =db.insert(constantes.DB_tablename,null, resgitroNuevo);

               return l;

            }catch (Exception e){
                Toast.makeText(mainActivity, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }




        }
        return 0;


    }
}
