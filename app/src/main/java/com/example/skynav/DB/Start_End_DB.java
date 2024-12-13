package com.example.skynav.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.skynav.MainActivity;

public class Start_End_DB {
    Context mainActivity;
    private SQLiteDatabase db;


    public Start_End_DB(Context mainActivity1) {
        this.mainActivity=mainActivity1;
    }

    //Este método inicializa la base de datos
    public SQLiteDatabase inicializarBD(){

        try{
            DBSkyNav dbSkyNav = new DBSkyNav(mainActivity);
            db = dbSkyNav.getWritableDatabase();
            return db;
        }catch (Exception e){
            Log.println(Log.ASSERT,"Error DB",""+e.getMessage());
        }
        return null;
    }
    public void finalizarDB(){
        db.close();
    }
}
