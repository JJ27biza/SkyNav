package com.example.skynav;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.example.skynav.Adaptadores.AdaptadorTipos;

public class MainActivity2 extends AppCompatActivity {

  private  ListView lvTipo;
  private Intent intent;
    private static final int LLAMADA_TELEFONO_DIRECTA = 0;
    private static final int REQUEST_CODE_1 = 1;
    private  AdaptadorTipos adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        lvTipo=findViewById(R.id.lvTipos);
        adaptador = new AdaptadorTipos(this,R.layout.activity_adaptador_tipos,getResources().getStringArray(R.array.tipos_vuelos),getResources().obtainTypedArray(R.array.fotos_tipos_vuelos));
        clickAdaptador();
    }
//Método para inflar el menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuUser = menu.findItem(R.id.menu_user);
        menuUser.setVisible(true);
        return true;
    }
    //Este método realiza una acción al clickar un  item
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.acerca){

            acercade();

        }else if (id==R.id.finalizar){

            finalizar();
        }else if (id==R.id.llamada){
            llamada();
        }else if(id==R.id.web) {
            pagina();
        }else if(id==R.id.compras){
            compras();

        }else if(id==R.id.perfil){
            perfil();

        }

        return true;
    }

    //Método para finalizar la activity
    public void OnclickFinalizar(View view) {
        Intent intent=getIntent();
        setResult(RESULT_OK,intent);
        finish();
    }
    //Este método crea un dialog con los datos de la app
    private void acercade(){
        AlertDialog.Builder ventana=  new AlertDialog.Builder(this);
        ventana.setIcon(R.drawable.travel1);
        ventana.setMessage("App desarrollada por Daniel\nIES TEIS\n1.0.0");
        ventana.setTitle("ACERCA DE");

        ventana.show();


    }
    //Método que crea una dialog preguntando si quiere finalizar la app
    private void finalizar(){

        AlertDialog.Builder ventana=  new AlertDialog.Builder(this);
        ventana.setIcon(R.drawable.travel1);
        ventana.setMessage("La app se va a cerrar\n¿Estas seguro?");
        ventana.setTitle("CIERRE DE APP");
        ventana.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which){
                //todo operaciones correspondientes
                finishAffinity();
            }
        });
        ventana.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ventana.show();
    }

    //Método para realizar la llamada
    private void llamada(){

        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {


                    intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:(+34)684191057"));
                    startActivity(intent);

                } else {


                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, LLAMADA_TELEFONO_DIRECTA);

                }
            }
        } else {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:(+34)684191057"));
            startActivity(intent);
        }

    }
    //El método crea una página automaticamente con una url en especifica
    private void pagina(){

        intent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.ejemplo.com/vuelos"));

        if(intent.resolveActivity(getPackageManager())!=null) {
            startActivity(intent);
        }else {//irresoluble
            Toast.makeText(this,"Esta accion no se puede resolver",Toast.LENGTH_LONG).show();

        }

    }
    //Método que cambia de activity
    private void compras(){

        Intent intent1=new Intent();
        intent1.setClass(MainActivity2.this,MainActivity7.class);
        startActivityForResult(intent1,REQUEST_CODE_1);
    }
    //Método que cambia de activity
    private void perfil(){
        Intent intent1=new Intent();
        intent1.setClass(MainActivity2.this,MainActivity8.class);
        startActivityForResult(intent1,REQUEST_CODE_1);
    }

//Método que realiza una acción al clicar un item de la listview
    private void clickAdaptador(){

        lvTipo.setAdapter(adaptador);
        lvTipo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String elemento=parent.getItemAtPosition(position).toString();
                Intent intent=new Intent();
                intent.setClass(MainActivity2.this, MainActivity3.class);
                intent.putExtra("tipo",elemento);
                startActivity(intent);
            }
        });

    }

}
