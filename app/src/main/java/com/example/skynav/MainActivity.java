package com.example.skynav;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skynav.Class.ExtensionsApp;
import com.example.skynav.DB.ConsultDB;
import com.example.skynav.DB.InsertDB;
import com.example.skynav.DB.Start_End_DB;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_1 = 1;
    private static final int NOTIFICATION_1=1;
    private static final int LLAMADA_TELEFONO_DIRECTA = 0;
    private LinearLayout linearSecundario;
    private SQLiteDatabase db;
    private LinearLayout linearIMG;
    private EditText etNombre;
    private EditText etUsuario;
    private EditText etContrasenha;

    private TextView txtInicio;
    private TextView txtCrear;
    private ImageView img;
    private Intent intent;
    private Button btnCrear;
    private Button btnIniciar;
   private Uri soundUri;
   private Start_End_DB startEndDB= startEndDB=new Start_End_DB(this);
   private ExtensionsApp extensionsApp =new ExtensionsApp(this);
   private InsertDB insertDB;
   private ConsultDB consultDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        db=startEndDB.inicializarBD();
        insertDB=new InsertDB(this,db);
        consultDB=new ConsultDB(this,db);
        inicializar();
        linearSecundario.setVisibility(GONE);
        txtInicio.setVisibility(GONE);
        txtCrear.setVisibility(GONE);
        onLongClick();
         soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.soundm);
    }
    //Este método infla el menu con el menu que hemos creado
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //Método que permite hacer acciones al clickar en cada item del menu
    /*****************************CODIGO_LIMPIO EVITAR UTILIZAR TANTOS IF**************************/
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.acerca){

            extensionsApp.acercade();

        }else if (id==R.id.finalizar){
            if(extensionsApp.finalizar()){finishAffinity();}
        }else if (id==R.id.llamada){
            llamada();
        }else if(id==R.id.web){
            pagina();
        }
        return true;
    }


    //Método que llamada al metodo insertarUsuario pasando por parametro los datos de las EditText
    public void onClickDBCrear(View view) {
        if(insertDB.insertarUsuario(etNombre.getText().toString(),etUsuario.getText().toString(), etContrasenha.getText().toString())==-1){

            Toast.makeText(this,"Error al insertar en la base",Toast.LENGTH_SHORT).show();

        }else{

            linearIMG.setVisibility(View.VISIBLE);
            linearSecundario.setVisibility(GONE);
            txtCrear.setVisibility(GONE);
            txtInicio.setVisibility(GONE);
            borrarTexto();
            notificacion();

        }


    }

    //Método que llamada a comprobarUsuario pasando por parametro los datos de las EditText
    public void onClickDBInicio(View view) {
        if(consultDB.comprobarUsuario(etUsuario.getText().toString(), etContrasenha.getText().toString()).equals(etContrasenha.getText().toString())){
            Intent intent=new Intent();
            intent.setClass(MainActivity.this, MainActivity2.class);
            startActivityForResult(intent,REQUEST_CODE_1);
            guardarId(consultDB.idUsuario);


        }else {

            Toast.makeText(this, "Inicio incorrecto", Toast.LENGTH_SHORT).show();

        }
    }

    //Este método que hace invisible unos componentes y pone visible otros componentes
    public void onClickVolver(View view) {
        linearIMG.setVisibility(View.VISIBLE);
        linearSecundario.setVisibility(GONE);
        txtCrear.setVisibility(GONE);
        txtInicio.setVisibility(GONE);
        borrarTexto();
    }
    //Método que pone en blanco los EditText
    private void borrarTexto(){
        etNombre.setText("");
        etUsuario.setText("");
        etContrasenha.setText("");



    }



    //Método que crea una dialog con datos de nuestra app

    //Este método crea una dialog preguntando si queremos finalizar la app


//cierra la base de datos al activarse
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
    //Este activityResult finaliza la activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_1) {
            if (resultCode == RESULT_OK) {

                boolean resultadoBooleano = data != null && data.getBooleanExtra("resultado_booleano", false);

                if(!resultadoBooleano==true){
                    finish();

                }
            }
        }
    }
    //Método que infla un menu contextual
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
    }
    //Este método al clickar en un item hace una acción
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();


        switch (item.getItemId()) {
            case R.id.action_crear:
               actionCrearUsuario();
                break;


            case R.id.action_inicio:

                actionInicioUsuario();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
    //Método que crea un menu contextual al hacer un click largo en la imagen
    private void onLongClick(){

        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                registerForContextMenu(img);
                return false;
            }
        });

    }
    //Método quue inicializa los componentes
    private void inicializar(){

        linearSecundario=findViewById(R.id.linearSecundario);
        linearIMG=findViewById(R.id.linearImg);
        etNombre=findViewById(R.id.etnombre);
        etUsuario=findViewById(R.id.etusuario);
        etContrasenha =findViewById(R.id.etcontrasenha);
        img=findViewById(R.id.travel);
        txtInicio=findViewById(R.id.inicioSesion);
        txtCrear=findViewById(R.id.crearSesión);
        btnIniciar=findViewById(R.id.btnDBInicio);
        btnCrear=findViewById(R.id.btnDBCrear);

    }
    //Método que permite hacer las llamadas
    private void llamada(){

        if (Build.VERSION.SDK_INT >= 23) {

                if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {


                    intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:(+34)684191057"));
                    startActivity(intent);

                } else {


                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, LLAMADA_TELEFONO_DIRECTA);

                }
        } else {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:(+34)684191057"));
            startActivity(intent);
        }

    }
//Método para aplicar el permiso de llamada
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==LLAMADA_TELEFONO_DIRECTA){

            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:(+34)684191057"));
                startActivity(intent);

            }
            else {
                Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Este método permite hacer una busqueda a una url automaticamente
    private void pagina(){

        intent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.ejemplo.com/vuelos"));

        if(intent.resolveActivity(getPackageManager())!=null) {
            startActivity(intent);
        }else {
            Toast.makeText(this,"Esta accion no se puede resolver",Toast.LENGTH_LONG).show();

        }

    }
    //El método al visualiza los componentes para crear un usuario
    private void actionCrearUsuario(){
        linearSecundario.setVisibility(View.VISIBLE);
        linearIMG.setVisibility(GONE);
        btnCrear.setVisibility(View.VISIBLE);
        btnIniciar.setVisibility(GONE);

        etNombre.setVisibility(View.VISIBLE);
        txtInicio.setVisibility(GONE);
        txtCrear.setVisibility(View.VISIBLE);


    }
    //El método al visualiza los componentes para iniciar un usuario
    private void actionInicioUsuario(){
        linearSecundario.setVisibility(View.VISIBLE);
        linearIMG.setVisibility(GONE);
        etNombre.setVisibility(GONE);
        txtInicio.setVisibility(View.VISIBLE);
        btnCrear.setVisibility(GONE);
        btnIniciar.setVisibility(View.VISIBLE);
        txtCrear.setVisibility(GONE);
    }
    //Método para realizar una notificación
    private void notificacion(){

        Notification.Builder builder = new Notification.Builder(this);

        builder.setSmallIcon(R.drawable.travel1);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.travel1);
        builder.setLargeIcon(bitmap);

        builder.setContentTitle("Bienvenida");
        builder.setContentText("¡Ya estás registrado como miembro de SkyNav!");

        if (Build.VERSION.SDK_INT >= 23) {

                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(soundUri);

        } else {
            builder.setSound(soundUri);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notificacion = builder.build();
        notificationManager.notify(NOTIFICATION_1, notificacion);

    }
    //Método que guarda el id del usuario
    /*************************SHARED_PREFERENCE**************************/
    private void guardarId(int id){

        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferenciasDB", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id",id);
        editor.apply();
    }



}