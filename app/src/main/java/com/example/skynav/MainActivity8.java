package com.example.skynav;

import static android.view.View.GONE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.skynav.Constantes.Constantes;
import com.example.skynav.DB.DBSkyNav;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity8 extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private Constantes constantes= new Constantes();
    private SQLiteDatabase db;
    private SharedPreferences sharedPreferences2;

    private ImageView imgPerfil;
    private int idRecibido;
    private EditText etnombre;
    private EditText etcorreo;
    private EditText etelefono;
    private  EditText etedad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        imgPerfil=findViewById(R.id.imgPerfil);
        etnombre=findViewById(R.id.etnombrePerfil);
        etcorreo=findViewById(R.id.etcorreoPerfil);
        etelefono=findViewById(R.id.etTelefonoPerfil);
        etedad=findViewById(R.id.etEdadPerfil);
       inicializarBD();
        clickPerfil();
        obteneridUsuario();
        cargarPerfil();
    }
    //Método que llama a openGallery al clickar en la imagen
    private void clickPerfil(){

        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }
    //Método para cargar el perfil en la base de datos
    private void cargarPerfil(){

        String[] recuperarConsultas = {constantes.perfil_column2, constantes.perfil_column3,constantes.perfil_column4,constantes.perfil_column5,constantes.perfil_column6};
        Cursor cursor = db.query(constantes.DB_tablename_4, recuperarConsultas, constantes.perfil_column1+"="+idRecibido, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(0);
                String correo=cursor.getString(1);
                int telefono=cursor.getInt(2);
                int edad=cursor.getInt(3);
                byte[] imagen=cursor.getBlob(4);
               etnombre.setText(nombre);
                etcorreo.setText(correo);
                etelefono.setText(String.valueOf(telefono));
                etedad.setText(String.valueOf(edad));
                establecerBitmapEnImageView( obtenerBitmapDesdeBytes(imagen),imgPerfil);



            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    //Método para abrir la galeria del movil
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
//Método para poner la foto en la ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgPerfil.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //Este método finaliza la activity
    public void onClickVolver(View view) {
        finish();
    }

    //Método para actualizar el contenido del perfil
    public void onClickActualizar(View view) {
        if(etnombre.getText().toString().isEmpty() || etcorreo.getText().toString().isEmpty() || etelefono.getText().toString().isEmpty() || etedad.getText().toString().isEmpty()) {
            Toast.makeText(this, "Campos incompletos", Toast.LENGTH_SHORT).show();
        } else {


            insertarPerfil(idRecibido, etnombre.getText().toString(), etcorreo.getText().toString(), Integer.parseInt(etelefono.getText().toString()), Integer.parseInt(etedad.getText().toString()),obtenerBytesDeImageView(imgPerfil));
        }

    }
    //Permite insertar el perfil en la base de datos
    private void insertarPerfil(int id,String nombre,String correo,int telefono,int edad,byte[] imagen){

        ContentValues resgitroNuevo = new ContentValues();
            try{
                resgitroNuevo.put(constantes.perfil_column1,id);
                resgitroNuevo.put(constantes.perfil_column2,nombre);
                resgitroNuevo.put(constantes.perfil_column3,correo);
                resgitroNuevo.put(constantes.perfil_column4,telefono);
                resgitroNuevo.put(constantes.perfil_column5,edad);
                resgitroNuevo.put(constantes.perfil_column6,imagen);

                long l =db.insert(constantes.DB_tablename_4,null, resgitroNuevo);

                if(l==-1){


                    actualizar(id, nombre,correo, telefono, edad,imagen);

                }else{
                    Toast.makeText(this, "Se a guardado", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                Toast.makeText(this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }
        //Método para obtener el id del usuario que utilizamos
    private void obteneridUsuario(){
        sharedPreferences2 = getSharedPreferences("MisPreferenciasDB", MODE_PRIVATE);
        idRecibido = sharedPreferences2.getInt("id", 0);

    }
    //Este método actualiza el contenido en la base de datos
    private void actualizar(int id,String nombre,String correo,int telefono,int edad,byte[] imagen){
        ContentValues registroActualizado = new ContentValues();
        registroActualizado.put(constantes.perfil_column2, nombre);
        registroActualizado.put(constantes.perfil_column3, correo);
        registroActualizado.put(constantes.perfil_column4, telefono);
        registroActualizado.put(constantes.perfil_column5, edad);
        registroActualizado.put(constantes.perfil_column6, imagen);
        long l = db.update(constantes.DB_tablename_4, registroActualizado, constantes.perfil_column1 + "=" + id, null);
        if(l==-1){

            Toast.makeText(this, "Error en la actualización", Toast.LENGTH_SHORT).show();

        }

    }
    //Método para inicializar la base de datos
    private void inicializarBD(){
        try {
            DBSkyNav dbSkyNav = new DBSkyNav(MainActivity8.this);
            db = dbSkyNav.getWritableDatabase();
        }catch (Exception e){
            Log.println(Log.ASSERT,"Error",""+e.getMessage());
        }
    }
    //Método para que al destruir la activity se cierre la base de datos
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
    //Combierte la imagen en un array de bytes para almacenar en la base de datos
    public byte[] obtenerBytesDeImageView(ImageView imageView) {

        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    //Método que obtiene una Bitmap de un array de byte
    public Bitmap obtenerBitmapDesdeBytes(byte[] bytesImagen) {
        return BitmapFactory.decodeByteArray(bytesImagen, 0, bytesImagen.length);
    }
    //Este método establece un Bitmap en una imagen
    public void establecerBitmapEnImageView(Bitmap bitmap, ImageView imageView) {
        imageView.setImageBitmap(bitmap);
    }
}