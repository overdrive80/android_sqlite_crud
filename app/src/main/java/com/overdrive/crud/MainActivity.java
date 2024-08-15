package com.overdrive.crud;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

// Clase MainActivity
public class MainActivity extends AppCompatActivity {
    private Button btnBuscar, btnActualizar, btnInsertar, btnBorrar;
    private EditText txtID, txtNombre, txtApellidos;
    private static final String TAG = "BBDD";
    private PersonaDAO personaDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bloqueamos la orientación vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Inicializar PersonaDAO
        personaDAO = new PersonaDAO(this);

        // Referenciamos los elementos del layout
        mapearObjetosLayout();

        // Eliminar la BBDD previa y crear una nueva conexión y BBDD
        eliminarBaseDeDatos();
        poblarBaseDeDatos(); // Añadir valores por defecto

        // Establecer listeners
        setListeners();
    }

    private void setListeners() {
        setListenerInsertar();
        setListenerBuscar();
        setListenerActualizar();
        setListenerBorrar();
    }



    private void resetTextView() {
        txtID.setText("");
        txtNombre.setText("");
        txtApellidos.setText("");
    }

    @Override
    protected void onDestroy() {
        personaDAO.getConexion().close();
        super.onDestroy();
    }

    /**
     * ---------------------------- LISTENERS ----------------------------
     **/
    private void setListenerBorrar() {
        btnBorrar.setOnClickListener(v -> {
            try {
                int id = Integer.parseInt(txtID.getText().toString());
                Persona persona = personaDAO.buscarPersona(id);

                personaDAO.borrarPersona(persona);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "El ID debe ser un número", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            resetTextView();
        });
    }
    private void setListenerInsertar() {
        btnInsertar.setOnClickListener(v -> {
            try {
                int id = Integer.parseInt(txtID.getText().toString());
                String nombre = txtNombre.getText().toString();
                String apellidos = txtApellidos.getText().toString();

                Persona persona = new Persona(id, nombre, apellidos);
                personaDAO.insertarPersona(persona);
                resetTextView();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "El ID debe ser un número", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListenerActualizar() {
        btnActualizar.setOnClickListener(v -> {

            try {
                int id = Integer.parseInt(txtID.getText().toString());
                Persona persona = personaDAO.buscarPersona(id);

                persona.setNombre(txtNombre.getText().toString());
                persona.setApellido(txtApellidos.getText().toString());

                personaDAO.actualizarPersona(persona);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "El ID debe ser un número", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            resetTextView();
        });
    }

    private void setListenerBuscar() {
        btnBuscar.setOnClickListener(v -> {

            try {
                int id = Integer.parseInt(txtID.getText().toString());
                Persona persona = personaDAO.buscarPersona(id);

                //Mostramos los valores en los campos del formulario
                txtNombre.setText(persona.getNombre());
                txtApellidos.setText(persona.getApellido());

            } catch (NumberFormatException e) {
                Toast.makeText(this, "El ID debe ser un número", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }


    /**
     * ---------------------------- MAPEOS ----------------------------
     **/
    private void mapearObjetosLayout() {
        btnBuscar = findViewById(R.id.btnBuscar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnInsertar = findViewById(R.id.btnInsertar);
        btnBorrar = findViewById(R.id.btnBorrar);

        txtID = findViewById(R.id.txtID);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellidos = findViewById(R.id.txtApellidos);
    }

    /**
     * ---------------------------- GESTIONES SOBRE LA BBDD ---------------------------
     **/
    private void poblarBaseDeDatos() {
        Persona p1 = new Persona(1, "Juan", "Pérez");
        Persona p2 = new Persona(2, "María", "López");
        Persona p3 = new Persona(3, "Pedro", "Gómez");

        personaDAO.insertarPersona(p1);
        personaDAO.insertarPersona(p2);
        personaDAO.insertarPersona(p3);
    }

    public void eliminarBaseDeDatos() {
        File database = getBaseDeDatos();

        if (database.exists() && database.delete()) {
            Log.d(TAG, "Base de datos eliminada");
        } else {
            Log.d(TAG, "No se pudo eliminar la base de datos");
        }
    }

    private File getBaseDeDatos() {
        return getApplicationContext().getDatabasePath(BBDD.NOMBRE_BBDD);
    }


}