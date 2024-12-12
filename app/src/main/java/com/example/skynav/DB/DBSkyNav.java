package com.example.skynav.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DBSkyNav extends SQLiteAssetHelper {




    private static final String DATABASE_NAME = "dbSkyNav.db";
    private static final int DATABASE_VERSION = 1;
    public DBSkyNav(Context context) {
        super(context, DATABASE_NAME,null,null, DATABASE_VERSION);
    }


}
