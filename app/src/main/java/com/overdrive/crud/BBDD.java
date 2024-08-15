package com.overdrive.crud;

import android.provider.BaseColumns;

public class BBDD {
    private static final String TIPO_TEXTO = " TEXT";
    private static final String SEP = ",";
    public static final String NOMBRE_BBDD = "personas.db";

    public BBDD() {
    }

    /* Clase interna que define el contenido de una tabla */
    public static class Personas implements BaseColumns {
        public static final String NOMBRE_TABLA = "personas";
        public static final String COL_ID = "id";
        public static final String COL_NOMBRE = "nombre";
        public static final String COL_APELLIDO = "apellido";

        /* Consultas DDL */
        public static final String CREAR_TABLA_PERSONAS =
                "CREATE TABLE " + Personas.NOMBRE_TABLA + " (" +
                        Personas.COL_ID + " INTEGER PRIMARY KEY," +
                        Personas.COL_NOMBRE + TIPO_TEXTO + SEP +
                        Personas.COL_APELLIDO + TIPO_TEXTO + ")";

        public static final String BORRAR_TABLA_PERSONAS =
                "DROP TABLE IF EXISTS " + Personas.NOMBRE_TABLA;
    }

}



