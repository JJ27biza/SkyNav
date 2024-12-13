package com.example.skynav.DB;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.skynav.Constantes.Constantes;
import com.example.skynav.MainActivity;
import com.example.skynav.MainActivity2;

public class ConsultDB {
    private Constantes constantes= new Constantes();
    private SQLiteDatabase db;
    private Context context;
    private Start_End_DB startEndDb= new Start_End_DB(context);
    public int idUsuario=0;




    public ConsultDB(Context main,SQLiteDatabase dbMain) {
        this.context=main;
        this.db=dbMain;

    }

    //Método que comprueba si el usuario al registrarse es el correcto
    public String comprobarUsuario(String nombre,String constraseña){

        if(nombre.toString().isEmpty()||constraseña.toString().isEmpty()){


            Toast.makeText(context,"Campo vacio",Toast.LENGTH_LONG).show();
        }else{

            String[] campoRecuperar={constantes.colum4,constantes.colum1};
            Cursor cursor=  db.query(constantes.DB_tablename,campoRecuperar,constantes.colum3+"="+"'"+nombre+"'",null,null,null,null);
            if(cursor.moveToFirst()){

                String resultado=cursor.getString(0);
                int id= cursor.getInt(1);
                idUsuario=id;
                //cursor.close();
                return resultado;


            }else{
                Toast.makeText(context, "Dato inexistente" ,Toast.LENGTH_SHORT).show();
                cursor.close();
                return "";

            }




        }
       return "";

    }

}
