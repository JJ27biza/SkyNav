package com.example.skynav;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import com.example.skynav.Constantes.Constantes;
import com.example.skynav.DB.DBSkyNav;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_1 = 1;

    private static final int NOTIFICATION_1=1;
    private static final int LLAMADA_TELEFONO_DIRECTA = 0;
    private SQLiteDatabase db;
    private LinearLayout linearSecundario;
    private ContentValues resgitroNuevo = new ContentValues();
    private LinearLayout linearIMG;
    private EditText etNombre;
    private EditText etUsuario;
    private EditText etContraseña;

    private TextView txtInicio;
    private TextView txtCrear;
    private ImageView img;
    private Intent intent;

    private Button btnCrear;
    private Button btnIniciar;
    private Constantes constantes= new Constantes();
   private Uri soundUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inicializarBD();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.acerca){

            acercade();

        }else if (id==R.id.finalizar){

            finalizar();
        }else if (id==R.id.llamada){
            llamada();
        }else if(id==R.id.web){
            pagina();
        }
        return true;
    }


    //Método que llamada al metodo insertarUsuario pasando por parametro los datos de las EditText
    public void onClickDBCrear(View view) {

        insertarUsuario(etNombre.getText().toString(),etUsuario.getText().toString(),etContraseña.getText().toString());

    }

    //Método que llamada a comprobarUsuario pasando por parametro los datos de las EditText
    public void onClickDBInicio(View view) {
        comprobarUsuario(etUsuario.getText().toString(),etContraseña.getText().toString());
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
        etContraseña.setText("");



    }

    //Este método que insterta en la base de datos los datos indicados y hace visible y invisible algunos componentes
    //y cuando se crea se envia una notificación
    private void insertarUsuario(String nombre,String usuario,String contraseña){

        if(nombre.isEmpty()||usuario.isEmpty()||contraseña.isEmpty()){
            Toast.makeText(this, "Campo vacio", Toast.LENGTH_SHORT).show();
        }else{
            try{
                resgitroNuevo.put(constantes.colum2,nombre);
                resgitroNuevo.put(constantes.colum3,usuario);
                resgitroNuevo.put(constantes.colum4,contraseña);
                long l =db.insert(constantes.DB_tablename,null, resgitroNuevo);

                if(l==-1){

                    Toast.makeText(this,"Error al insertar en la base",Toast.LENGTH_SHORT).show();

                }else{

                    linearIMG.setVisibility(View.VISIBLE);
                    linearSecundario.setVisibility(GONE);
                    txtCrear.setVisibility(GONE);
                    txtInicio.setVisibility(GONE);
                    borrarTexto();
                    notificacion();

                }

            }catch (Exception e){
                Toast.makeText(this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }




        }


    }
    //Método que comprueba si el usuario al registrarse es el correcto
    private void comprobarUsuario(String nombre,String constraseña){

        if(nombre.toString().isEmpty()||constraseña.toString().isEmpty()){


            Toast.makeText(this,"Campo vacio",Toast.LENGTH_LONG).show();
        }else{

            String[] campoRecuperar={constantes.colum4,constantes.colum1};
            Cursor cursor= db.query(constantes.DB_tablename,campoRecuperar,constantes.colum3+"="+"'"+nombre+"'",null,null,null,null);
            if(cursor.moveToFirst()){

                String resultado=cursor.getString(0);

                if(resultado.equals(constraseña)){
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,MainActivity2.class);
                    startActivityForResult(intent,REQUEST_CODE_1);
                    guardarId(cursor.getInt(1));


                }else {

                    Toast.makeText(this, "Inicio incorrecto", Toast.LENGTH_SHORT).show();

                }


            }else{
                Toast.makeText(this, "Dato inexistente" ,Toast.LENGTH_SHORT).show();


            }
            cursor.close();


        }


    }
    //Método que crea una dialog con datos de nuestra app
    private void acercade(){
        AlertDialog.Builder ventana=  new AlertDialog.Builder(this);
        ventana.setIcon(R.drawable.travel1);
        ventana.setMessage("App desarrollada por Daniel\nIES TEIS\n1.0.0");
        ventana.setTitle("ACERCA DE");

        ventana.show();


    }
    //Este método crea una dialog preguntando si queremos finalizar la app
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
        etContraseña=findViewById(R.id.etcontraseña);
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
    private void guardarId(int id){

        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferenciasDB", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id",id);
        editor.apply();
    }
    //Este método inicializa la base de datos
    private void inicializarBD(){

        try{
            DBSkyNav dbSkyNav = new DBSkyNav(MainActivity.this);
            db = dbSkyNav.getWritableDatabase();

        }catch (Exception e){
            Log.println(Log.ASSERT,"Error DB",""+e.getMessage());
        }
    }



}