package com.example.skynav;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.skynav.Adaptadores.CustomSpinnerAdapter;
import com.example.skynav.Constantes.Constantes;
import com.example.skynav.DB.DBSkyNav;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity6 extends AppCompatActivity {

    private EditText etNombreTarjeta;
    private EditText etNumeroTarjeta;
    private EditText etCCV;
    private Spinner spMes;
    private Spinner spAno;
    private CheckBox chkGuardar;

    private ArrayList<EditText> arrayEt=new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences2;
    private SharedPreferences sharedPreferences3;
    private Uri soundUri;
    private Intent intent;
    private Constantes constantes=new Constantes();
    private SQLiteDatabase db;
    private int idRecibido;
    private String nombreAvion;
    private String origen;
    private String destino;
    private String fecha;


    private int month;
    private int year;
    int añoSeleccionado ;
    int mesSeleccionado ;


    private static final int NOTIFICATION_1=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //obtener fecha de del calendario
        intent=getIntent();
        fecha=intent.getStringExtra("fecha");
        inicializarDB();
        obtenerid();
        obtenerVehiculo();
        limpiar();
        inicializar();
        almacenarEt();
        //sonido de notificación
        sharedPreferences = getSharedPreferences("nombre_preferencias", Context.MODE_PRIVATE);
        soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.soundm);
        guardarDatos();
        aplicarGuardado();
        actionSp();





    }


//Método que permite realizar la llamada pero comprueba la fecha de la tarjeta
    public void onClickComprar(View view) {
        if(comprobarCampos()) {

            if(month>mesSeleccionado||year<añoSeleccionado){
                Toast.makeText(this, "Fecha invalida", Toast.LENGTH_SHORT).show();
            }else{
                guardarViaje();
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
                notificacion();


            }


        }
    }
    //Método para obtener el id del usuario
    private void obtenerid(){
        sharedPreferences2 = getSharedPreferences("MisPreferenciasDB", MODE_PRIVATE);
        idRecibido = sharedPreferences2.getInt("id", 0);

    }
    //Método para obtener el vehiculo al que vamos a realizar la compra con origen y destino
    private void obtenerVehiculo(){

        sharedPreferences3 = getSharedPreferences("Avion", MODE_PRIVATE);

        nombreAvion=sharedPreferences3.getString("nombre","0");
        origen=sharedPreferences3.getString("origen","0");
        destino=sharedPreferences3.getString("destino","0");
        SharedPreferences.Editor editor = sharedPreferences3.edit();
        editor.clear();
        editor.apply();
    }
    //Este método inicializar varios componentes
    private void inicializar(){

        etNombreTarjeta=findViewById(R.id.etNombreTarjeta);
        etNumeroTarjeta=findViewById(R.id.etNumeroTarjeta);
        etCCV=findViewById(R.id.etCCV);
        spMes=findViewById(R.id.spMes);
        spAno=findViewById(R.id.spAño);
        chkGuardar=findViewById(R.id.chkGuardarTarjeta);
        String[] items = getResources().getStringArray(R.array.mes);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, items);
        spMes.setAdapter(adapter);

        String[] items2 = getResources().getStringArray(R.array.ano);
        CustomSpinnerAdapter adapter1 = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, items2);
        spAno.setAdapter(adapter1);

        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        añoSeleccionado=Integer.parseInt(spAno.getSelectedItem().toString());
        mesSeleccionado=Integer.parseInt(spMes.getSelectedItem().toString());
    }
    //Método que comprueba que todos los campos tienen texto
    private boolean comprobarCampos(){
        for(int a=0;a<arrayEt.size();a++){

            if(arrayEt.get(a).getText().toString().isEmpty()){

                Toast.makeText(this, "Campos Vacios", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;

    }
    //Este método almacena en un array los elementos de la tarjeta
    private void almacenarEt(){

        arrayEt.add(etNombreTarjeta);
        arrayEt.add(etNumeroTarjeta);
        arrayEt.add(etCCV);

    }
    //Método que guarda el contenido de la tarjeta al checkear el check
    private void guardarDatos(){

        chkGuardar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chkGuardar.isChecked()){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nombreAdulto1", etNombreTarjeta.getText().toString());
                    editor.putString("apellidoAdulto1", etNumeroTarjeta.getText().toString());
                    editor.putString("nombreContacto1", etCCV.getText().toString());

                    editor.putBoolean("check1",true);
                    editor.apply();
                }else if(!chkGuardar.isChecked()){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("nombreAdulto1");
                    editor.remove("apellidoAdulto1");
                    editor.remove("nombreContacto1");

                    editor.remove("check1");
                    editor.apply();
                }
            }
        });
    }
    //Este método aplica el guardado en lo EditText
    private void aplicarGuardado(){

        String nombreAdulto = sharedPreferences.getString("nombreAdulto1", "");
        String apellidoAdulto = sharedPreferences.getString("apellidoAdulto1", "");
        String nombreContacto = sharedPreferences.getString("nombreContacto1", "");


        etNombreTarjeta.setText(nombreAdulto);
        etNumeroTarjeta.setText(apellidoAdulto);
        etCCV.setText(nombreContacto);



        Boolean valor=sharedPreferences.getBoolean("check1",false);
        if(valor==true){
            chkGuardar.setChecked(true);

        }else{
            chkGuardar.setChecked(false);
        }

    }
    //Método que limpia los editText
    private void limpiar(){

        for(int a=0;a<arrayEt.size();a++){

            if(!(arrayEt.get(a).getText().toString().isEmpty())){

                arrayEt.get(a).setText("");
            }

        }

    }
    //Este método lanza las notificaciones
    private void notificacion(){

        Notification.Builder builder = new Notification.Builder(this);

        builder.setSmallIcon(R.drawable.travel1);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.travel1);
        builder.setLargeIcon(bitmap);

        builder.setContentTitle("Compra ejectuada");
        builder.setContentText("¡Ya está listo su viaje, buen viaje!");

        if (Build.VERSION.SDK_INT >= 23) {

                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(soundUri);

        } else {
            builder.setSound(soundUri);
        }
        Intent intent1=new Intent();
        intent1.setClass(MainActivity6.this,MainActivity7.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1, PendingIntent.FLAG_IMMUTABLE);
        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notificacion = builder.build();
        notificationManager.notify(NOTIFICATION_1, notificacion);

    }
    //Método que guarda en la base de datos los viajes
    private void guardarDBViajes(int idUsuario,String nombreVehiculo,String fecha,String origen,String destino){
        ContentValues resgitroNuevo = new ContentValues();

        try{
                resgitroNuevo.put(constantes.viajes_column1,idUsuario);
                resgitroNuevo.put(constantes.viajes_column2,nombreVehiculo);
                resgitroNuevo.put(constantes.viajes_column3,fecha);
                resgitroNuevo.put(constantes.viajes_column4,origen);
                resgitroNuevo.put(constantes.viajes_column5,destino);
                long l =db.insert(constantes.DB_tablename_3,null, resgitroNuevo);

                if(l==-1){

                    Toast.makeText(this,"Error al insertar en la base",Toast.LENGTH_SHORT).show();

                }

            }catch (Exception e){
                Toast.makeText(this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }




        }
        //Método que guarda los viajes
        private void guardarViaje(){

            guardarDBViajes(idRecibido,nombreAvion,fecha,origen,destino);

        }
        //Este método inicializa la base de datos
        private void inicializarDB(){

            try {
                DBSkyNav dbSkyNav = new DBSkyNav(MainActivity6.this);
                db = dbSkyNav.getWritableDatabase();
            }catch (Exception e){
                Log.println(Log.ASSERT,"Error",""+e.getMessage());
            }
        }
        //Método de destrucción que cierra la base de datos
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
    //Método que cambia valores de variables al cambiar los spinners
    private void actionSp(){
        spAno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String año =parent.getItemAtPosition(position).toString();
                añoSeleccionado=Integer.parseInt(año);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String mes =parent.getItemAtPosition(position).toString();
                mesSeleccionado=Integer.parseInt(mes);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }




    }
