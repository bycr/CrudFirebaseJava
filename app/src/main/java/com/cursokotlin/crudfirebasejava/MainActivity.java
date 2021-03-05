package com.cursokotlin.crudfirebasejava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cursokotlin.crudfirebasejava.Adaptadores.ListViewPersonasAdapter;
import com.cursokotlin.crudfirebasejava.Models.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //paso 4 declaramos las variables
    //creamos un array list
    private ArrayList<Persona> listPersonas = new ArrayList<Persona>();
    //pasamos un arrayAdapter al list view
    ArrayAdapter<Persona> arrayAdapterPersona; //Puede usar este adaptador para proporcionar vistas
    ListViewPersonasAdapter listViewPersonasAdapter;
    LinearLayout linearLayoutEditar;
    ListView listViewPersonas;
    //componentes linearLayout del mainActivy
    EditText inputNombre, inputTelefono;
    Button btnCancelar;
    Persona personaSeleccionada;

    //luego de agregar el firebase desde tools
    //creamos las variables de tipo Firebase data base
    FirebaseDatabase firebaseDatabase; //para escribir y leer en la base de datos
    DatabaseReference databaseReference; //para poder crear los documentos o colecciones

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //paso 5 referenciamos los componentes inicializados
        inputNombre = findViewById(R.id.inputNombre);
        inputTelefono = findViewById(R.id.inputTelefono);
        btnCancelar = findViewById(R.id.btnCancelar);
        listViewPersonas = findViewById(R.id.listViewPersonas);
        linearLayoutEditar = findViewById(R.id.linearLayoutEditar);

        //paso 7, evento del listView
        listViewPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //hacemos un casting de la clase persona
                //y enviamos los datos
                personaSeleccionada = (Persona) parent.getItemAtPosition(position);
                inputNombre.setText(personaSeleccionada.getNombres());
                inputTelefono.setText(personaSeleccionada.getTelefono());
                //hacemos visible el linear layout
                linearLayoutEditar.setVisibility(View.VISIBLE);
            }
        });


        //paso 8, evento del boton cancelar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutEditar.setVisibility(View.GONE); //ocultamos el linearLayout
                personaSeleccionada = null;
            }
        });

        //paso 6 inicializamos el metodo de fireBase
        inicializarFirebase();
        //paso 9, crear metodo
        listarPersonas();

    }

    //paso 6 metodo de firebase
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //paso 9, creamos el metodo para mostrar en el listview
    private void listarPersonas() {
        //child significa coleccion
        databaseReference.child("Personas").addValueEventListener(new ValueEventListener() {
            @Override

            //paso 9.1 primero hacemos el dataChange
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPersonas.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Persona p = objSnaptshot.getValue(Persona.class);
                    listPersonas.add(p);
                }

                //paso 10
                //iniciamos nuestro adaptador
                listViewPersonasAdapter = new  ListViewPersonasAdapter(MainActivity.this, listPersonas);
                listViewPersonas.setAdapter(listViewPersonasAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //paso 11 inflamos el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crud_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //paso 12 hacemos cada una de las funcionalidades del crud
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String nombres = inputNombre.getText().toString();
        String telefono = inputTelefono.getText().toString();
        switch (item.getItemId()){
            //paso 13 metodo para guardar
            case R.id.menu_agregar:
                //paso 13 dentro de este va el paso 14 crear el metodo show error
                insertar();
                break;
                //paso 15 creamos el metodo para modificar a una persona
            case R.id.menu_editar:
                if (personaSeleccionada != null){
                    //paso 16 creamos el metodo validarInputs
                    if (validarInputs()==false){
                        Persona p = new Persona();
                        p.setIdpersona(personaSeleccionada.getIdpersona());
                        p.setNombres(nombres);
                        p.setTelefono(telefono);
                        databaseReference.child("Personas").child(p.getIdpersona()).setValue(p);
                        Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_LONG).show();
                        linearLayoutEditar.setVisibility(View.GONE);
                        personaSeleccionada = null;
                    }else {
                        Toast.makeText(this, "Selecciona una persona", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this, "Selecciona una persona", Toast.LENGTH_LONG).show();
                }
                break;
                //paso 17 metodo para eliminar
            case R.id.menu_eliminar:
                if (personaSeleccionada != null){
                    Persona p2 = new Persona();
                    p2.setIdpersona(personaSeleccionada.getIdpersona());
                    databaseReference.child("Personas").child(p2.getIdpersona()).removeValue();
                    linearLayoutEditar.setVisibility(View.GONE);
                    personaSeleccionada = null;
                    Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this, "Seleccione una persona para eliminar", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //paso 13 metodo para el alerdialog
    public void insertar(){
        //inicializamos el alertDialog
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                MainActivity.this
        );

        //inicializamos las vistas del Layout
        View mView = getLayoutInflater().inflate(R.layout.insertar, null);
        Button btnInsertar = (Button) mView.findViewById(R.id.btnInsertar);

        final EditText mInputNombres = (EditText) mView.findViewById(R.id.inputNombre);
        final EditText mInputTelefono = (EditText) mView.findViewById(R.id.inputTelefono);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        //capturamos los datos del alertDialog
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombres = mInputNombres.getText().toString();
                String telefono = mInputTelefono.getText().toString();

                if(nombres.isEmpty() || nombres.length()<3){ //validamos los datos
                    //implementacion paso 14
                    showError(mInputNombres, "Nombre invalido (Min 3 letras)");
                }else if(telefono.isEmpty() || telefono.length()<9 ){ //validamos los datos
                    //implementacion paso 14
                    showError(mInputTelefono, "Telefono invalido (Min 9 números)");
                }else {
                    Persona p = new Persona();
                    p.setIdpersona(UUID.randomUUID().toString());
                    p.setNombres(nombres);
                    p.setTelefono(telefono);
                    databaseReference.child("Personas").child(p.getIdpersona()).setValue(p);
                    Toast.makeText(MainActivity.this, "Registrado correctamente",
                            Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
    }

    //paso 14 creamos el metodo showError
    public void showError(EditText input, String s) {
        input.requestFocus();
        input.setError(s);
    }

    //paso 17 creamos el metodo validar imputs
    private boolean validarInputs() {
        String nombre = inputNombre.getText().toString();
        String telefono = inputTelefono.getText().toString();

        if (nombre.isEmpty() || nombre.length() < 3 ){
            showError(inputNombre, "Nombre invalido. (Min 3 letras)");
            return true;
        }else if(telefono.isEmpty() || telefono.length() < 9){
            showError(inputTelefono, "Telefono invalido (Min 9 números)");
            return true;
        }
        else {
            return false;
        }
    }
}