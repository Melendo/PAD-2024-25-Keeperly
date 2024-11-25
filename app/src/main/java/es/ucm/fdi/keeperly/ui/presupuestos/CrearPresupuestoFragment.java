package es.ucm.fdi.keeperly.ui.presupuestos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Date;

import es.ucm.fdi.keeperly.R;


public class CrearPresupuestoFragment extends Fragment {

    private PresupuestosViewModel presupuestosViewModel;
    private EditText etNombre, etCantidad, etFechaInicio, etFechaFin;
    private Spinner etCategoria;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crear_presupuesto, container, false);

        // Inicializamos las vistas
        etNombre = rootView.findViewById(R.id.etNombre);
        etCategoria = rootView.findViewById(R.id.spCategoria); // Si es un Spinner
        etCantidad = rootView.findViewById(R.id.etCantidad);
        etFechaInicio = rootView.findViewById(R.id.etFechaInicio);
        etFechaFin = rootView.findViewById(R.id.etFechaFin);

        // Inicializamos el ViewModel
        presupuestosViewModel = new ViewModelProvider(this).get(PresupuestosViewModel.class);

        // Configurar el botón de crear presupuesto
        Button btnCrear = rootView.findViewById(R.id.btnCrear);
        btnCrear.setOnClickListener(v -> {
            // Obtenemos los datos del formulario
            String nombre = etNombre.getText().toString();
            int categoria = Integer.parseInt(etCategoria.getSelectedItem().toString()); // Suponiendo que es un Spinner
            double cantidad = Double.parseDouble(etCantidad.getText().toString());
            Date fechaInicio = Date.valueOf(etFechaInicio.getText().toString());
            Date fechaFin = Date.valueOf(etFechaFin.getText().toString());
            int usuario = 1;

            // Llamamos al método del ViewModel para crear el presupuesto
            presupuestosViewModel.crearPresupuesto(nombre, usuario , categoria, cantidad, fechaInicio, fechaFin);
        });

        return rootView;
    }
}