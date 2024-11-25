package es.ucm.fdi.keeperly.ui.presupuestos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.sql.Date;
import java.util.Calendar;

import es.ucm.fdi.keeperly.R;


public class CrearPresupuestoFragment extends Fragment {

    private PresupuestosViewModel presupuestosViewModel;
    private EditText etNombre, etCantidad, etFechaInicio, etFechaFin;
    private Spinner spCategoria;
    private Button btnCrear, btnCancelar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crear_presupuesto, container, false);

        // Inicializamos las vistas
        etNombre = rootView.findViewById(R.id.etNombre);
        spCategoria = rootView.findViewById(R.id.spCategoria); // Si es un Spinner
        etCantidad = rootView.findViewById(R.id.etCantidad);

        //Fechas
        etFechaInicio = rootView.findViewById(R.id.etFechaInicio);
        etFechaFin = rootView.findViewById(R.id.etFechaFin);

        etFechaInicio.setOnClickListener(v -> showDatePickerDialog(etFechaInicio));
        etFechaFin.setOnClickListener(v -> showDatePickerDialog(etFechaFin));

        // Inicializamos el ViewModel
        presupuestosViewModel = new ViewModelProvider(this).get(PresupuestosViewModel.class);

        // Configurar el botón de crear presupuesto
        btnCrear = rootView.findViewById(R.id.btnCrear);
        btnCrear.setOnClickListener(v -> {
            if (validarCampos()) {
                // Obtenemos los datos del formulario
                String nombre = etNombre.getText().toString();
                int usuario = 1;
                int categoria = Integer.parseInt(spCategoria.getSelectedItem().toString()); // Suponiendo que es un Spinner
                double cantidad = Double.parseDouble(etCantidad.getText().toString());
                Date fechaInicio = Date.valueOf(etFechaInicio.getText().toString());
                Date fechaFin = Date.valueOf(etFechaFin.getText().toString());


                // Llamamos al método del ViewModel para crear el presupuesto

                presupuestosViewModel.crearPresupuesto(nombre, usuario, categoria, cantidad, fechaInicio, fechaFin);
            }
        });

        btnCancelar = rootView.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> {
            // Aquí podrías limpiar los campos o cerrar el fragment
            limpiarCampos();
            Toast.makeText(requireContext(), "Operación cancelada", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack(); // Redirige a la vista anterior
        });

        // Observa el estado del insert
        presupuestosViewModel.getInsertStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                switch (status) {
                    case 1: // Éxito
                        Toast.makeText(getContext(), "Presupuesto creado con éxito", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack(); // Redirige a la vista anterior
                        break;
                    case -1: // Error de base de datos
                        Toast.makeText(getContext(), "Error al insertar el presupuesto", Toast.LENGTH_SHORT).show();
                        break;
                    case -2: // Otro error (opcional)
                        Toast.makeText(getContext(), "Error: La cantidad debe ser mayor que 0", Toast.LENGTH_SHORT).show();
                        break;
                    case -3: // Otro error (opcional)
                        Toast.makeText(getContext(), "Error: Fecha Fin debe ser posterior a Fecha Inicio", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getContext(), "Error desconocido", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return rootView;
    }

    private void showDatePickerDialog(EditText editText) {
        // Obtener la fecha actual
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth) -> {
                    // Formatear la fecha seleccionada
                    String fechaSeleccionada = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                    editText.setText(fechaSeleccionada);
                },
                year, month, day
        );


        // Mostrar el diálogo
        datePickerDialog.show();
    }


    private boolean validarCampos() {
        if (etNombre.getText().toString().isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            return false;
        }

        if (etCantidad.getText().toString().isEmpty()) {
            etCantidad.setError("La cantidad es obligatoria");
            return false;
        }

        if (etFechaInicio.getText().toString().isEmpty()) {
            etFechaInicio.setError("La fecha de inicio es obligatoria");
            return false;
        }

        if (etFechaFin.getText().toString().isEmpty()) {
            etFechaFin.setError("La fecha de fin es obligatoria");
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etCantidad.setText("");
        etFechaInicio.setText("");
        etFechaFin.setText("");
        spCategoria.setSelection(0);
    }

}