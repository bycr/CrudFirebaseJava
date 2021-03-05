package com.cursokotlin.crudfirebasejava.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cursokotlin.crudfirebasejava.Models.Persona;
import com.cursokotlin.crudfirebasejava.R;

import java.util.ArrayList;

//al implementar baseAdapter implementamos los 4 metodos con alt + enter
public class ListViewPersonasAdapter extends BaseAdapter {

    //paso 3 creamos el adaptador de para conectar la vista y los datos
    Context context; //es una clase abstracta para acceso a recurso especificos
    ArrayList<Persona> personaData;
    LayoutInflater layoutInflater; //para inflar una vista
    Persona personaModel;

    //paso 3.1
    //insertamos el contexto y arrayList
    public ListViewPersonasAdapter(Context context, ArrayList<Persona> personaData) {
        this.context = context;
        this.personaData = personaData;
        //paso 3.2
        layoutInflater = (LayoutInflater)context.getSystemService(
                context.LAYOUT_INFLATER_SERVICE
        );
    }

    //paso 3.3
    @Override //retornamos el tamaño de la lista personas
    public int getCount() {
        return personaData.size(); //retornamos tamaño de lista de personas
    }

    //paso 3.4
    @Override
    public Object getItem(int position) {
        return personaData.get(position); //obtenemos la posicion de la persona en la lista
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //paso 3.5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null){
            rowView = layoutInflater.inflate(R.layout.lista_personas,
                    null,
                    true);
        }

        //enlazamos las vistas
        TextView nombres = rowView.findViewById(R.id.nombres);
        TextView telefono = rowView.findViewById(R.id.telefono);

        personaModel = personaData.get(position);

        nombres.setText(personaModel.getNombres());
        telefono.setText(personaModel.getTelefono());

        return rowView;
    }
}
