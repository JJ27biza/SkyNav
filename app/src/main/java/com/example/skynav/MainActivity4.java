package com.example.skynav;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skynav.Adaptadores.CustomSpinnerAdapter;

import java.util.ArrayList;

public class MainActivity4 extends AppCompatActivity {
    private static final int REQUEST_CODE_1 = 1;
    private static final int REQUEST_CODE_2 = 2;

    private EditText etNombreAdulto;
    private EditText etApellidoAdulto;
    private EditText etNombreContacto;
    private EditText etApellidoContacto;
    private AutoCompleteTextView etPais;
    private EditText etPrefijo;
    private EditText etNumero;
    private CheckBox checkBox;
    private RadioButton rbPremium;
    private RadioButton rbClasica;
    private Spinner spNumero;
    private TextView txtPrecio;
    private TextView txtFecha;
    private Intent intent;
    private Intent intent1;
    private String precio;
    private String plazas;
    private String txtSumaPersonas;
    private SharedPreferences sharedPreferences;
    private String accion="1";
    private ArrayList<EditText> arrayEt= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inicializar();
        limpiarEt();
        almacenarEt();
        actionSpinner();
        actionRadioButton();
        intent=getIntent();
        precio=intent.getStringExtra("precio").toString();
        plazas=intent.getStringExtra("plazas").toString();
        txtSumaPersonas=precio;
        txtPrecio.setText(precio+"€");
        sharedPreferences = getSharedPreferences("nombre_preferencias", Context.MODE_PRIVATE);
        guardarDatos();
        aplicarGuardado();



    }

    //Método que hace las comprobaciones para realizar la compra
    public void onClickComprar(View view) {
        String prefijo=etPrefijo.getText().toString();
        String maximo=spNumero.getSelectedItem().toString();
        int numero1=Integer.valueOf(maximo);
        int numero3=Integer.valueOf(plazas);

        if(formatoPrefijo(prefijo)==false){

            AlertDialogPrefijo();
        }
        else if(numero3<numero1){

            AlertDialogMaximoNumero();

        }else if(comprobacionCampos()==true){

            if(comprobarFecha()==true){
                Intent intent3= new Intent();
                intent3.setClass(MainActivity4.this, MainActivity6.class);
                intent3.putExtra("fecha",txtFecha.getText().toString());
                startActivityForResult(intent3,REQUEST_CODE_2);


            }
        }

    }
//Este método finaliza la activity
    public void onClickVolver(View view) {
        finish();
    }
    //El método inicializa los componentes y inicializa el arrayAdapter para el autocompletetext y cambia el color al spinner
    private void inicializar(){

        etNombreAdulto=findViewById(R.id.etNombreAdulto);
        etApellidoAdulto=findViewById(R.id.etApellidoAdulto);
        etNombreContacto=findViewById(R.id.etNombreContacto);
        etApellidoContacto=findViewById(R.id.etApellidoContacto);
        etPais=findViewById(R.id.Pais);
        etPrefijo=findViewById(R.id.prefijo);
        etNumero=findViewById(R.id.movil);
        checkBox=findViewById(R.id.chkGuardar);
        rbPremium=findViewById(R.id.rbPremium);
        rbClasica=findViewById(R.id.rbClasica);
        spNumero=findViewById(R.id.spPasajeros);
        txtPrecio=findViewById(R.id.Precio);
        txtFecha=findViewById(R.id.txtFecha);
        String[] countries = getResources().getStringArray(R.array.countries_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, countries);
        etPais.setAdapter(adapter);
        etPais.setThreshold(1);

        String[] items = getResources().getStringArray(R.array.pasajeros);
        CustomSpinnerAdapter adapterSp = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, items);
        spNumero.setAdapter(adapterSp);




    }
    //El método realiza una dialog para el numero maximo de plazas
    private void AlertDialogMaximoNumero(){

        AlertDialog.Builder ventana=  new AlertDialog.Builder(this);
        ventana.setIcon(R.drawable.travel1);
        ventana.setMessage("Exceso el limite del numero de personas");
        ventana.setTitle("Error");
        ventana.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ventana.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        ventana.show();

    }
    //El método realiza una dialog para comprobar el prefijo
    private void AlertDialogPrefijo(){
        AlertDialog.Builder ventana=  new AlertDialog.Builder(this);
        ventana.setIcon(R.drawable.travel1);
        ventana.setMessage("Formato del prefijo incorrecto");
        ventana.setTitle("Error");
        ventana.setPositiveButton("Corregir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ventana.show();


    }
    //Método que comprueba que el prefijo tenga el formato correcto
    private boolean formatoPrefijo(String prefijo){
        String regex = "\\+\\d{2}";


        if(prefijo.matches(regex)){

            return true;


        }else{

            return false;
        }

    }
    //El método almacena en el array los componentes necesarios
    private void almacenarEt(){

        arrayEt.add(etApellidoAdulto);
        arrayEt.add(etPais);
        arrayEt.add(etNumero);
        arrayEt.add(etPrefijo);
        arrayEt.add(etNombreAdulto);
        arrayEt.add(etNombreContacto);
        arrayEt.add(etApellidoContacto);



    }
    //Método que comprueba que los campos no esten vacios
    private boolean comprobacionCampos(){

        for(int a=0;a<arrayEt.size();a++){

            if(arrayEt.get(a).getText().toString().isEmpty()){

                Toast.makeText(this, "Campos Vacios", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;
    }

//Método que realiza acciones del cambio de precio al cambiar el numero de los spinners
    private void actionSpinner(){

        spNumero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 accion = parent.getItemAtPosition(position).toString();

                int numero = Integer.valueOf(precio);
                int numero2 = Integer.valueOf(accion);
                int calculo = numero * numero2;
                if(rbPremium.isChecked()){
                    calculo += 30;
                }

                txtPrecio.setText(calculo + " €");

                String maximo = spNumero.getSelectedItem().toString();
                int numero1 = Integer.valueOf(maximo);
                int numero3 = Integer.valueOf(plazas);
                if(numero3 < numero1){
                    AlertDialogMaximoNumero();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Método que realiza acciones del cambio de precio al cambiar el radiobutton
    private void actionRadioButton(){

        rbPremium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(rbPremium.isChecked()){
                    int numero=Integer.valueOf(precio);
                    int numeroAccion=Integer.valueOf(accion);
                    int suma=(numero*numeroAccion)+30;
                    String total=String.valueOf(suma);
                    txtPrecio.setText(total+"€");


                }

            }
        });
        rbClasica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rbClasica.isChecked()){
                    String numerotxt=spNumero.getSelectedItem().toString();
                    int numero=Integer.valueOf(numerotxt);
                    int numero2=Integer.valueOf(precio);
                    int suma=numero2*numero;
                    String sumatxt=String.valueOf(suma);
                    txtPrecio.setText(sumatxt+"€");


                }

            }
        });

    }
    //Método que permite utilizar el sharedpreference al checkear el checkBox
    private void guardarDatos(){

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked()){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nombreAdulto", etNombreAdulto.getText().toString());
                    editor.putString("apellidoAdulto", etApellidoAdulto.getText().toString());
                    editor.putString("nombreContacto", etNombreContacto.getText().toString());
                    editor.putString("apellidoContacto", etApellidoContacto.getText().toString());
                    editor.putString("pais", etPais.getText().toString());
                    editor.putString("prefijo", etPrefijo.getText().toString());
                    editor.putString("movil", etNumero.getText().toString());
                    editor.putBoolean("check",true);
                    editor.apply();
                }else if(!checkBox.isChecked()){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("nombreAdulto");
                    editor.remove("apellidoAdulto");
                    editor.remove("nombreContacto");
                    editor.remove("apellidoContacto");
                    editor.remove("pais");
                    editor.remove("prefijo");
                    editor.remove("movil");
                    editor.remove("check");
                    editor.apply();
                }
            }
        });
    }
    //Método que aplica el guardado en los sharedPreferences si el check esta a true
    private void aplicarGuardado(){

        String nombreAdulto = sharedPreferences.getString("nombreAdulto", "");
        String apellidoAdulto = sharedPreferences.getString("apellidoAdulto", "");
        String nombreContacto = sharedPreferences.getString("nombreContacto", "");
        String apellidoContacto = sharedPreferences.getString("apellidoContacto", "");

        String pais = sharedPreferences.getString("pais", "");
        String prefijo = sharedPreferences.getString("prefijo", "");
        String numero = sharedPreferences.getString("movil", "");

            etNombreAdulto.setText(nombreAdulto);
            etApellidoAdulto.setText(apellidoAdulto);
            etNombreContacto.setText(nombreContacto);
            etApellidoContacto.setText(apellidoContacto);
            etPais.setText(pais);
            etPrefijo.setText(prefijo);
            etNumero.setText(numero);


        Boolean valor=sharedPreferences.getBoolean("check",false);
        if(valor==true){
            checkBox.setChecked(true);

        }else{
            checkBox.setChecked(false);
        }

    }
    //Método que permite limpiar los EditText
    private void limpiarEt(){

        etNombreAdulto.setText("");
        etApellidoAdulto.setText("");
        etNombreContacto.setText("");
        etApellidoContacto.setText("");
        etPais.setText("");
        etNumero.setText("");
        etPrefijo.setText("");
    }

    //Método que vamos al la activity donde tenemos el calendario
    public void onClickCalendario(View view) {
        intent1 = new Intent();
        intent1.setClass(MainActivity4.this, MainActivity5.class);
        startActivityForResult(intent1, REQUEST_CODE_1);

    }
    //Método que obtiene la fecha del calendario
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_1) {
            if (resultCode == RESULT_OK) {

                String fecha=data.getStringExtra("fecha");
                txtFecha.setText(fecha);


            }
        }else if(requestCode==REQUEST_CODE_2){

            if(resultCode==RESULT_OK){

                finish();
            }
        }
    }
//Comprueba que la fecha esta indicada si no crea un dialog
    private boolean comprobarFecha(){

        if(txtFecha.getText().toString().isEmpty()){
            AlertDialog.Builder ventana=  new AlertDialog.Builder(this);
            ventana.setIcon(R.drawable.travel1);
            ventana.setMessage("Fecha sin introducir");
            ventana.setTitle("Error");
            ventana.show();
            return false;

        }
        return true;

    }


}