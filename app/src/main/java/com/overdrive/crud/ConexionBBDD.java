package com.overdrive.crud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ConexionBBDD extends SQLiteOpenHelper {
    private static final String TAG = "ConexionBBDD";

    public ConexionBBDD(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = BBDD.Personas.CREAR_TABLA_PERSONAS;
        Log.d(TAG, "onCreate: " + sql);
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: Database Version: " + oldVersion + " -> " + newVersion);
        db.execSQL(BBDD.Personas.BORRAR_TABLA_PERSONAS);
        onCreate(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}

