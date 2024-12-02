package es.ucm.fdi.keeperly.ui.presupuestos;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.ui.categorias.CategoriasViewModel;



public class CrearPresupuestoFragment extends Fragment {

    private PresupuestosViewModel presupuestosViewModel;
    private CategoriasViewModel categoriaViewModel;

    private EditText etNombre, etCantidad, etFechaInicio, etFechaFin;
    private Spinner spCategoria;
    private String categoriaSeleccionada;
    private Button btnCrear, btnCancelar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crear_presupuesto, container, false);

        // Inicializamos el ViewModel
        presupuestosViewModel = new ViewModelProvider(this).get(PresupuestosViewModel.class);

        // Inicializamos las vistas
        etNombre = rootView.findViewById(R.id.etNombre);
        spCategoria = rootView.findViewById(R.id.spCategoria); // Si es un Spinner
        etCantidad = rootView.findViewById(R.id.etCantidad);

        categoriaViewModel = new ViewModelProvider(this).get(CategoriasViewModel.class);
        categoriaViewModel.getCategorias().observe(getViewLifecycleOwner(), categorias -> {
            if (categorias != null) {
                // Crea una lista de nombres de categorías
                List<String> nombresCategorias = new ArrayList<>();
                nombresCategorias.add(0, "Selecciona una categoría");
                for (Categoria categoria : categorias) {
                    nombresCategorias.add(categoria.getNombre());
                }

                // Configura el adaptador
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        nombresCategorias
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCategoria.setAdapter(adapter);
            }
        });

        spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    categoriaSeleccionada = parent.getItemAtPosition(position).toString();
                } else {
                    categoriaSeleccionada = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Fechas
        etFechaInicio = rootView.findViewById(R.id.etFechaInicio);
        etFechaFin = rootView.findViewById(R.id.etFechaFin);

        etFechaInicio.setOnClickListener(v -> showDatePickerDialog(etFechaInicio));
        etFechaFin.setOnClickListener(v -> showDatePickerDialog(etFechaFin));

        // Configurar el botón de crear presupuesto
        btnCrear = rootView.findViewById(R.id.btnCrear);
        btnCrear.setOnClickListener(v -> {
            if (validarCampos()) {
                // Obtenemos los datos del formulario
                String nombre = etNombre.getText().toString();
                double cantidad = Double.parseDouble(etCantidad.getText().toString());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date fechaInicio, fechaFin;
                try {
                    fechaInicio = dateFormat.parse(etFechaInicio.getText().toString());
                    fechaFin = dateFormat.parse(etFechaFin.getText().toString());

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }


                // Llamamos al método del ViewModel para crear el presupuesto
                int idCat = categoriaViewModel.getCategoriaByNombre(categoriaSeleccionada).getId();
                presupuestosViewModel.crearPresupuesto(nombre, idCat, cantidad, fechaInicio, fechaFin);
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
        presupuestosViewModel.getOperationStatus().observe(getViewLifecycleOwner(), status -> {
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
                    String fechaSeleccionada = String.format("%02d-%02d-%04d", dayOfMonth, month1 + 1, year1);
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

        if (categoriaSeleccionada.isEmpty()) {
            Toast.makeText(requireContext(), "Debes seleccionar una categoría", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etCantidad.getText().toString().isEmpty() || Double.parseDouble(etCantidad.getText().toString()) <= 0) {
            etCantidad.setError("La cantidad es obligatoria y debe ser mayor que 0");
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