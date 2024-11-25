package es.ucm.fdi.keeperly.ui.presupuestos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Date;

import es.ucm.fdi.keeperly.R;


public class CrearPresupuestoFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String nombre;
    private Integer id_usu;
    private Integer id_categoria;
    private Double cantidad;
    private Date fecha_ini;
    private Date fecha_fin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_presupuesto, container, false);
    }
}