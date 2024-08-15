package com.overdrive.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class PersonaDAO {
    private ConexionBBDD conexion;
    private Context context;
    private static final String TAG = "BBDD";

    public PersonaDAO(Context context) {
        this.context = context;
        conexion = new ConexionBBDD(context, BBDD.NOMBRE_BBDD, 1);

        verificarConexion();
    }

    private void verificarConexion() {
        SQLiteDatabase db = conexion.getReadableDatabase();

        if (db != null) {
            Log.d(TAG, "Base de datos abierta correctamente.");
        } else {
            Log.d(TAG, "Error al abrir la base de datos.");
        }
    }

    /**
     * ---------------------------- OPERACIONES CRUD ----------------------------
     **/
    public void borrarPersona(Persona persona) throws Exception {
        SQLiteDatabase db = conexion.getWritableDatabase();

        //Criterios de filtrado
        String where = BBDD.Personas.COL_ID + " = ?";
        String[] whereArgs = {"" + persona.getId()};

        //Consulta de actualización.
        int filasEliminadas = db.delete(
                BBDD.Personas.NOMBRE_TABLA,
                where,
                whereArgs);

        if (filasEliminadas > 0) {
            Toast.makeText(context, "Se ha eliminado el registro con ID: " + persona.getId(), Toast.LENGTH_SHORT).show();
        } else {
            throw new Exception("No se ha encontrado el registro");
        }
    }

    public void actualizarPersona(Persona persona) throws Exception {
        SQLiteDatabase db = conexion.getWritableDatabase();

        // Crear un mapa de valores, donde el nombre de las columnas son las claves
        ContentValues valores = new ContentValues();
        valores.put(BBDD.Personas.COL_NOMBRE, persona.getNombre());
        valores.put(BBDD.Personas.COL_APELLIDO, persona.getApellido());

        //Criterios de filtrado
        String where = BBDD.Personas.COL_ID + " = ?";
        String[] whereArgs = {"" + persona.getId()};

        //Consulta de actualización.
        int filasActualizadas = db.update(
                BBDD.Personas.NOMBRE_TABLA,
                valores,
                where,
                whereArgs);

        if (filasActualizadas > 0) {
            Toast.makeText(context, "Se ha actualizado el registro con ID: " + persona.getId(), Toast.LENGTH_SHORT).show();
        } else {
            throw new Exception("No se ha encontrado el registro");
        }

    }

    public Persona buscarPersona(int id) throws Exception {
        SQLiteDatabase db = conexion.getReadableDatabase();

        //Definimos la consulta
        String[] camposSelect = null;  // Para que devuelva todas
        String where = BBDD.Personas.COL_ID + " = ?";
        String[] whereArgs = {"" + id};
        String orderBy = null;
        String groupBy = null;
        String having = null;

        //Estructura try-with-resources. Se emplea para cerrar automáticamente el cursor
        try (Cursor cursor = db.query(
                BBDD.Personas.NOMBRE_TABLA,     // La tabla a consultar
                camposSelect,                   // Columnas a devolver (pasa null para obtener todas)
                where,                          // Las columnas para la cláusula WHERE
                whereArgs,                      // Los valores para la cláusula WHERE
                groupBy,                        // Agrupar las filas
                having,                         // Filtrar por grupos de filas
                orderBy                         // Orden de clasificación
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                return new Persona(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );

            } else {
                throw new Exception("No se ha encontrado el registro");
            }
        }
    }

    public void insertarPersona(Persona persona) {
        // Insertamos la persona en la BBDD
        SQLiteDatabase db = conexion.getWritableDatabase();

        // Crear un mapa de valores, donde el nombre de las columnas son las claves
        ContentValues valores = new ContentValues();
        valores.put(BBDD.Personas.COL_ID, persona.getId());
        valores.put(BBDD.Personas.COL_NOMBRE, persona.getNombre());
        valores.put(BBDD.Personas.COL_APELLIDO, persona.getApellido());

        String mensaje = "";

        try {
            long nuevoID = db.insert(BBDD.Personas.NOMBRE_TABLA, null, valores);

            if (nuevoID == -1) {
                mensaje = "Error al insertar el registro";
            } else {
                mensaje = "Registro guardado con ID: " + nuevoID;
            }

        } catch (SQLiteConstraintException e) {
            mensaje = "Error al insertar el registro";
        }

        Log.d(TAG, mensaje);
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }

    public ConexionBBDD getConexion() {
        return conexion;
    }

    public int getUltimoID() {

        SQLiteDatabase db = conexion.getReadableDatabase();

        String[] camposSelect = {"MAX(" + BBDD.Personas.COL_ID + ")"};
        String where = null;
        String[] whereArgs = null;
        String orderBy = null;
        String groupBy = null;
        String having = null;

        try (Cursor cursor = db.query(
                BBDD.Personas.NOMBRE_TABLA,     // La tabla a consultar
                camposSelect,                   // Columnas a devolver (pasa null para obtener todas)
                where,                          // Las columnas para la cláusula WHERE
                whereArgs,                      // Los valores para la cláusula WHERE
                groupBy,                        // Agrupar las filas
                having,                         // Filtrar por grupos de filas
                orderBy                         // Orden de clasificación
        )
        ) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            } else {
                return -1;
            }
        }

        // Se podría haber usado rawquery:
        // String query = "SELECT MAX(" + BBDD.Personas.COL_ID + ") AS max_id FROM " + BBDD.Personas.NOMBRE_TABLA;
        // Cursor cursor = db.rawQuery(query, null);
    }

}
