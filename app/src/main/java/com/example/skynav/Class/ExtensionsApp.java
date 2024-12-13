package com.example.skynav.Class;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.skynav.R;

public class ExtensionsApp {
    private Context context;
    private boolean finishAffinity;


    public ExtensionsApp(Context context1) {
        this.context=context1;
    }

    public void acercade(){
        AlertDialog.Builder ventana=  new AlertDialog.Builder(context);
        ventana.setIcon(R.drawable.travel1);
        ventana.setMessage("App desarrollada por Daniel\nIES TEIS\n1.0.0");
        ventana.setTitle("ACERCA DE");

        ventana.show();


    }
    public boolean finalizar(){

        AlertDialog.Builder ventana=  new AlertDialog.Builder(context);
        ventana.setIcon(R.drawable.travel1);
        ventana.setMessage("La app se va a cerrar\n¿Estas seguro?");
        ventana.setTitle("CIERRE DE APP");
        ventana.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which){
                //todo operaciones correspondientes
                finishAffinity=true;

            }
        });
        ventana.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity=false;

            }
        });
        ventana.show();
        return finishAffinity;
    }
}
