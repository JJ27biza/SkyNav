package com.example.skynav;

import static com.google.android.libraries.places.api.model.LocalDate.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.libraries.places.api.model.LocalDate;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.Calendar;
import java.util.Date;

public class MainActivity5 extends AppCompatActivity {

    private DatePicker calendar;
    private String fecha="";
    private int año;
    private int month;
    private int year;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);


        calendar = findViewById(R.id.calendar);
        año = 2025;
        calendar.setMinDate(getMinDateInMillis());

        Calendar currentCalendar = Calendar.getInstance();
        year = currentCalendar.get(Calendar.YEAR);
        month = currentCalendar.get(Calendar.MONTH);
        day = currentCalendar.get(Calendar.DAY_OF_MONTH);
    }

    // Método para obtener la fecha mínima en milisegundos
    private long getMinDateInMillis() {
        Calendar minDate = Calendar.getInstance();
        minDate.set(año, 0, 1);
        return minDate.getTimeInMillis();
    }

    // Método que se llama cuando se hace clic en el botón de fecha
    public void onClickFecha(View view) {

        if (year < calendar.getYear() ||
                (year == calendar.getYear() && month < calendar.getMonth()) ||
                (year == calendar.getYear() && month == calendar.getMonth() && day <= calendar.getDayOfMonth()) ||
                year > calendar.getYear()) {
            fecha = calendar.getDayOfMonth() + "/" + (calendar.getMonth() + 1) + "/" + calendar.getYear();
            Intent intent = getIntent();
            intent.putExtra("fecha", fecha);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "La fecha es incorrecta", Toast.LENGTH_SHORT).show();
        }
    }
}

