package es.ucm.fdi.keeperly.ui.presupuestos;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;
import es.ucm.fdi.keeperly.ui.categorias.CategoriasViewModel;
import es.ucm.fdi.keeperly.ui.login.LoginViewModel;

public class PresupuestoDetalladoFragment extends Fragment {

    private CategoriasViewModel categoriasViewModel;
    private PresupuestosViewModel presupuestosViewModel;
    private CategoriasViewModel categoriaViewModel;

    EditText etNombre;
    Spinner spCategoria; // Si es un Spinner
    EditText etCantidad;
    EditText etFechaInicio;
    EditText etFechaFin;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        categoriasViewModel = new ViewModelProvider(this).get(CategoriasViewModel.class);
        presupuestosViewModel = new ViewModelProvider(this).get(PresupuestosViewModel.class);

        return inflater.inflate(R.layout.fragment_presupuesto_detallado, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener referencias a los TextView

        TextView nombreTextView = view.findViewById(R.id.textViewNombreValor);
        TextView cantidadTextView = view.findViewById(R.id.textViewCantidadValor);
        TextView gastadoTextView = view.findViewById(R.id.textViewGastadoValor);
        TextView fechaInicioTextView = view.findViewById(R.id.textViewFechaInicioValor);
        TextView fechaFinTextView = view.findViewById(R.id.textViewFechaFinValor);
        TextView categoriaTextView = view.findViewById(R.id.textViewCategoriaValor);

        Presupuesto presupuesto = new Presupuesto();
        String nombre_categoria = "";
        // Obtener datos desde el Bundle
        Bundle args = getArguments();
        if (args != null) {
            //Cargar datos del presupuesto
            presupuesto.setId(args.getInt("id", 0));
            presupuesto.setNombre(args.getString("nombre", "N/A"));
            presupuesto.setCantidad(Double.parseDouble(args.getString("cantidad", "0.0")));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            try {
                presupuesto.setFechaInicio(dateFormat.parse(args.getString("fechaInicio", "N/A")));
                presupuesto.setFechaFin(dateFormat.parse(args.getString("fechaFin", "N/A")));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            double gastado = presupuestosViewModel.getTotalGastadoEnPresupuesto(presupuesto);
            presupuesto.setGastado(gastado * (-1));
            nombre_categoria = args.getString("categoria", "N/A");

            //Settear datos del presupuesto
            nombreTextView.setText(presupuesto.getNombre());
            cantidadTextView.setText(String.format("%.2f€", presupuesto.getCantidad()));
            gastadoTextView.setText(String.format("%.2f€",gastado));
            fechaInicioTextView.setText(dateFormat.format(presupuesto.getFechaInicio()));
            fechaFinTextView.setText(dateFormat.format(presupuesto.getFechaFin()));
            categoriaTextView.setText(args.getString("categoria", "N/A"));
        }

        //Obtener referencias a los botones
        Button buttonEliminar = view.findViewById(R.id.buttonEliminar);
        buttonEliminar.setOnClickListener(v -> eliminarPresupuesto(presupuesto));
        Button buttonEditar = view.findViewById(R.id.buttonEditar);
        String finalNombre_categoria = nombre_categoria;
        buttonEditar.setOnClickListener(v -> mostrarDialogoEditarPresupuesto(presupuesto, finalNombre_categoria));
    }

    void eliminarPresupuesto(Presupuesto presupuesto) {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Presupuesto")
                .setMessage("¿Seguro que deseas eliminar el presupuesto " + presupuesto.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    presupuestosViewModel.eliminarPresupuesto(presupuesto);
                    presupuestosViewModel.getDeleteStatus().observe(getViewLifecycleOwner(), status -> {
                        if (status != null) {
                            if (status) {
                                Toast.makeText(getContext(), "Presupuesto eliminado con éxito", Toast.LENGTH_SHORT).show();
                                getParentFragmentManager().popBackStack(); // Redirige a la vista anterior
                            } else {
                                Toast.makeText(getContext(), "Error al eliminar la categoria", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEditarPresupuesto(Presupuesto presupuesto, String nombre_categoria) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_editar_presupuesto, null);
        builder.setView(dialogView);

        // Referencias a los elementos del diálogo
        etNombre = dialogView.findViewById(R.id.etNombre);
        spCategoria = dialogView.findViewById(R.id.spCategoria); // Si es un Spinner
        etCantidad = dialogView.findViewById(R.id.etCantidad);
        etFechaInicio = dialogView.findViewById(R.id.etFechaInicio);
        etFechaFin = dialogView.findViewById(R.id.etFechaFin);

        categoriaViewModel = new ViewModelProvider(this).get(CategoriasViewModel.class);
        categoriaViewModel.getCategorias().observe(getViewLifecycleOwner(), categorias -> {
            if (categorias != null) {
                // Crea una lista de nombres de categorías
                List<String> nombresCategorias = new ArrayList<>();
                for (Categoria categoria : categorias) {
                    nombresCategorias.add(categoria.getNombre());
                }

                // Configura el adaptador
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        nombresCategorias
                );
                spCategoria.setSelection(adapter.getPosition(nombre_categoria));

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCategoria.setAdapter(adapter);
            }
        });

        etFechaInicio.setOnClickListener(v -> showDatePickerDialog(etFechaInicio));
        etFechaFin.setOnClickListener(v -> showDatePickerDialog(etFechaFin));

        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardar);

        etNombre.setText(presupuesto.getNombre());
        etCantidad.setText(String.valueOf(presupuesto.getCantidad()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        etFechaInicio.setText(dateFormat.format(presupuesto.getFechaInicio()));
        etFechaFin.setText(dateFormat.format(presupuesto.getFechaFin()));

        AlertDialog dialog = builder.create();
        dialog.show();

        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        // Botón Guardar
        btnGuardar.setOnClickListener(v -> {

            if (validarCampos()) {
                // Actualiza el nombre de la categoría
                Presupuesto presupuestoEditado = new Presupuesto();
                presupuestoEditado.setId(presupuesto.getId());
                presupuestoEditado.setNombre(etNombre.getText().toString());
                presupuestoEditado.setCantidad(Double.parseDouble(etCantidad.getText().toString()));
                try {
                    presupuestoEditado.setFechaInicio(dateFormat.parse(etFechaInicio.getText().toString()));
                    presupuestoEditado.setFechaFin(dateFormat.parse(etFechaFin.getText().toString()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                presupuestoEditado.setIdCategoria(categoriaViewModel.getCategoriaByNombre(spCategoria.getSelectedItem().toString()).getId());
                presupuestosViewModel.update(presupuestoEditado); // Llamar al método update del ViewModel
            }
        });

        // Observa el estado del insert
        presupuestosViewModel.getUpdateStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                switch (status) {
                    case 1: // Éxito
                        Toast.makeText(getContext(), "Presupuesto modificado con éxito", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        getParentFragmentManager().popBackStack(); // Redirige a la vista anterior
                        break;
                    case -1: // Error de base de datos
                        Toast.makeText(getContext(), "Error al modificar el presupuesto", Toast.LENGTH_SHORT).show();
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
}
